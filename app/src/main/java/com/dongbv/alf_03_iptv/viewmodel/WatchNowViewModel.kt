package com.dongbv.alf_03_iptv.viewmodel

import android.app.Application
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 5-slot player pool. Slots mapped to channel positions:
 *   [current-2]  [current-1]  [current]  [current+1]  [current+2]
 *   prepared     prepared     PLAYING     prepared      prepared
 *
 * On playback error the current player's listener emits autoNextEvent,
 * and the composable animates the pager to the next page.
 */
@UnstableApi
class WatchNowViewModel(
    application: Application,
    private val repository: AppRepository,
    private val streamCache: SimpleCache
) : AndroidViewModel(application) {

    val channels: StateFlow<List<ChannelEntity>> field=  MutableStateFlow<List<ChannelEntity>>(emptyList())
    val isMuted: StateFlow<Boolean> field = MutableStateFlow(false)

     val currentIndex:StateFlow<Int>  field = MutableStateFlow(0)

    private val _pagePlayerMap = MutableStateFlow<Map<Int, ExoPlayer>>(emptyMap())
    val pagePlayerMap: StateFlow<Map<Int, ExoPlayer>> = _pagePlayerMap.asStateFlow()

   
    private val _autoNextEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val autoNextEvent: SharedFlow<Int> = _autoNextEvent.asSharedFlow()


    private fun parseStreamUrl(raw: String): Pair<String, Map<String, String>> {
        val pipe = raw.indexOf('|')
        if (pipe < 0) return raw to emptyMap()
        val url = raw.substring(0, pipe)
        val headers = raw.substring(pipe + 1)
            .split('&')
            .mapNotNull { kv ->
                val eq = kv.indexOf('=')
                if (eq < 0) null else kv.substring(0, eq).trim() to kv.substring(eq + 1).trim()
            }
            .toMap()
        return url to headers
    }

   
   
   
    private fun mediaSourceFor(rawUrl: String): MediaSource {
        val (url, headers) = parseStreamUrl(rawUrl)
        if (url.startsWith("rtsp://", ignoreCase = true) ||
            url.startsWith("rtsps://", ignoreCase = true)) {
            Log.d(TAG, "protocol=RTSP  url=$url")
            return RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(url))
        }
        val http = DefaultHttpDataSource.Factory()
            .setUserAgent(BROWSER_UA)
            .setConnectTimeoutMs(15_000)
            .setReadTimeoutMs(15_000)
            .setAllowCrossProtocolRedirects(true)
        if (headers.isNotEmpty()) http.setDefaultRequestProperties(headers)
        return DefaultMediaSourceFactory(
            CacheDataSource.Factory()
                .setCache(streamCache)
                .setUpstreamDataSourceFactory(http)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        ).createMediaSource(MediaItem.fromUri(url))
    }

    private val playerPool: List<ExoPlayer> = List(5) {
        ExoPlayer.Builder(application).build().apply {
            playWhenReady = false
            volume = 0f
        }
    }
    private val freePool = ArrayDeque<ExoPlayer>().also { it.addAll(playerPool) }

   
   
   
   
    private val recentlyErrored = mutableSetOf<ExoPlayer>()

   
   
    private val retryCount = mutableMapOf<ExoPlayer, Int>()
    private val retryJobs = mutableMapOf<ExoPlayer, Job>()

    private fun isRetryableError(error: PlaybackException): Boolean {
        val causeClass = error.cause?.javaClass?.simpleName ?: ""
        return causeClass == "PlaylistStuckException" ||
               error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ||
               error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED ||
               error.errorCode == PlaybackException.ERROR_CODE_IO_READ_POSITION_OUT_OF_RANGE
    }

    init {
        playerPool.forEachIndexed { slot, player ->
            player.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    val page = _pagePlayerMap.value.entries.find { it.value === player }?.key
                    val rawUrl = channels.value.getOrNull(page ?: -1)?.url
                    val count  = retryCount.getOrDefault(player, 0)

                    Log.e(
                        TAG,
                        "POOL ERROR  slot=$slot  page=$page  retry=$count/$MAX_RETRIES" +
                        "  code=${error.errorCode}  cause=${error.cause?.javaClass?.simpleName}" +
                        "  url=$rawUrl",
                        error
                    )

                   
                    recentlyErrored += player

                    if (rawUrl != null && isRetryableError(error) && count < MAX_RETRIES) {
                        retryCount[player] = count + 1
                        val delayMs = 2_000L * (count + 1)  
                        Log.w(TAG, "retryable error — retry ${count+1}/$MAX_RETRIES in ${delayMs}ms  slot=$slot  page=$page")
                        retryJobs[player]?.cancel()
                        retryJobs[player] = viewModelScope.launch {
                            delay(delayMs)
                            val retryPage = page ?: return@launch
                            if (_pagePlayerMap.value[retryPage] !== player) return@launch
                            Log.d(TAG, "retrying  slot=$slot  page=$page")
                            val source = mediaSourceFor(rawUrl)
                            player.setMediaSource(source)
                            player.prepare()
                            if (player === _pagePlayerMap.value[currentIndex.value]) {
                                player.playWhenReady = true
                            }
                        }.also { job ->
                            job.invokeOnCompletion {
                                if (retryJobs[player] === job) retryJobs.remove(player)
                            }
                        }
                        return
                    }

                   
                    retryCount.remove(player)
                    if (player === _pagePlayerMap.value[currentIndex.value]) {
                        val next = currentIndex.value + 1
                        if (next <= channels.value.lastIndex) {
                            Log.w(TAG, "giving up → auto-next to page $next  slot=$slot")
                            _autoNextEvent.tryEmit(next)
                        }
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    val stateName = when (state) {
                        Player.STATE_IDLE      -> "IDLE"
                        Player.STATE_BUFFERING -> "BUFFERING"
                        Player.STATE_READY     -> "READY"
                        Player.STATE_ENDED     -> "ENDED"
                        else                   -> "UNKNOWN($state)"
                    }
                    val page = _pagePlayerMap.value.entries.find { it.value === player }?.key
                    Log.d(TAG, "state=$stateName  slot=$slot  page=$page  isCurrent=${player === _pagePlayerMap.value[currentIndex.value]}")

                    if (state == Player.STATE_READY) {
                        retryCount.remove(player) 
                        retryJobs.remove(player)?.cancel()
                    }

                    if (state == Player.STATE_IDLE) {
                       
                        if (recentlyErrored.remove(player)) return
                       
                        if (player === _pagePlayerMap.value[currentIndex.value]) {
                            val next = currentIndex.value + 1
                            if (next <= channels.value.lastIndex) {
                                Log.w(TAG, "silent stall  slot=$slot  page=$page  → auto-next to $next")
                                _autoNextEvent.tryEmit(next)
                            }
                        }
                    }
                }
            })
        }

        viewModelScope.launch {
            val all = repository.getAllChannels().first()
            channels.value = all.shuffled()
            if (all.isNotEmpty()) updateWindow(0)
        }
    }

    @OptIn(UnstableApi::class)
    fun updateWindow(index: Int) {
        val list = channels.value
        if (list.isEmpty()) return
        currentIndex.value = index

        val windowPositions = ((index - 2)..(index + 2)).filter { it in list.indices }
        val current = _pagePlayerMap.value.toMutableMap()

       
        val toEvict = current.keys.filter { it !in windowPositions }
        toEvict.forEach { pos ->
            current.remove(pos)?.also { p ->
                Log.d(TAG, "evict  page=$pos  slot=${playerPool.indexOf(p)}")
                retryCount.remove(p)
                retryJobs.remove(p)?.cancel()
                p.stop()
                p.clearMediaItems()
                freePool.addLast(p)
            }
        }

       
        windowPositions.forEach { pos ->
            if (pos !in current) {
                val player = freePool.removeFirstOrNull() ?: run {
                    Log.e(TAG, "freePool exhausted  cannot prepare page=$pos  freePool=${freePool.size}  window=$windowPositions")
                    return@forEach
                }
                Log.d(TAG, "prepare  page=$pos  slot=${playerPool.indexOf(player)}  url=${list[pos].url}")
                val source = mediaSourceFor(list[pos].url)
                player.setMediaSource(source)
                player.prepare()
                current[pos] = player
            }
        }

       
       
       
        windowPositions.forEach { pos ->
            val player = current[pos] ?: return@forEach
            if (player.playbackState == Player.STATE_IDLE) {
                Log.w(TAG, "re-prepare dead player  page=$pos  slot=${playerPool.indexOf(player)}  url=${list[pos].url}")
                val source = mediaSourceFor(list[pos].url)
                player.setMediaSource(source)
                player.prepare()
            }
        }

       
        val muted = isMuted.value
        current.forEach { (pos, player) ->
            if (pos == index) {
                player.volume = if (muted) 0f else 1f
                player.playWhenReady = true
            } else {
                player.volume = 0f
                player.playWhenReady = false
            }
        }

        _pagePlayerMap.value = current.toMap()
    }

    fun toggleMute() {
        val muted = !isMuted.value
        isMuted.value = muted
        _pagePlayerMap.value[currentIndex.value]?.volume = if (muted) 0f else 1f
    }

    fun toggleFavorite(channelId: Long) {
        viewModelScope.launch { repository.toggleFavorite(channelId) }
    }

    fun pausePlayback() {
        _pagePlayerMap.value[currentIndex.value]?.playWhenReady = false
    }

    fun resumePlayback() {
        if (channels.value.isNotEmpty()) {
            _pagePlayerMap.value[currentIndex.value]?.playWhenReady = true
        }
    }

    override fun onCleared() {
        retryJobs.values.forEach { it.cancel() }
        retryJobs.clear()
        playerPool.forEach { it.release() }
        super.onCleared()
    }

    companion object {
        private const val TAG = "WatchNowViewModel"
        private const val MAX_RETRIES = 3
        private const val BROWSER_UA =
            "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
    }
}
