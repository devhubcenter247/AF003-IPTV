package com.dongbv.alf_03_iptv.ui.main.play

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView

object PlayerManager {

    private var player: ExoPlayer? = null
    private val httpFactory = DefaultHttpDataSource.Factory()
        .setUserAgent(BROWSER_UA)
        .setConnectTimeoutMs(15_000)
        .setReadTimeoutMs(15_000)
        .setAllowCrossProtocolRedirects(true)
    private var currentUrl: String = ""
    private var currentChannelId: Long = 0
    private var currentChannelName: String = ""
    private var listener: PlayerListener? = null
    private var currentPlayerView: PlayerView? = null

    private var sleepTimer: CountDownTimer? = null
    private var sleepTimerListener: SleepTimerListener? = null
    private var remainingTimeMs: Long = 0
    private var targetTimeMs: Long = 0

    interface SleepTimerListener {
        fun onSleepTimerTick(remainingMs: Long)
        fun onSleepTimerFinish()
    }

    interface PlayerListener {
        fun onPlaybackStateChanged(state: Int)
        fun onIsPlayingChanged(isPlaying: Boolean)
        fun onPlayerError(error: PlaybackException)
    }

    fun initialize(context: Context) {
        if (player == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build()
            player = ExoPlayer.Builder(context.applicationContext)
                .setMediaSourceFactory(DefaultMediaSourceFactory(httpFactory))
                .build().apply {
                setAudioAttributes(audioAttributes, true)
                addListener(internalListener)
            }
        }
    }

    fun attachToView(playerView: PlayerView) {
        currentPlayerView = playerView
        playerView.player = player
    }

    fun detachFromView(playerView: PlayerView) {
        playerView.player = null
        if (currentPlayerView == playerView) currentPlayerView = null
    }

    fun isCurrentView(playerView: PlayerView): Boolean = currentPlayerView == playerView

    fun setListener(listener: PlayerListener?) {
        this.listener = listener
    }

    fun play(url: String, channelId: Long, channelName: String = "") {
        val state = player?.playbackState
        if (currentUrl == url && state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            player?.play()
            return
        }
        currentUrl = url
        currentChannelId = channelId
        currentChannelName = channelName

        val (cleanUrl, headers) = parseStreamUrl(url)
        val isRtsp = cleanUrl.startsWith("rtsp://", ignoreCase = true) ||
                     cleanUrl.startsWith("rtsps://", ignoreCase = true)

        val mediaItem = MediaItem.Builder()
            .setUri(cleanUrl)
            .setMediaMetadata(MediaMetadata.Builder().setTitle(channelName).build())
            .build()

        player?.let {
            if (isRtsp) {
                // Bypass the HTTP factory — RTSP is a separate protocol
                it.setMediaSource(RtspMediaSource.Factory().createMediaSource(mediaItem))
            } else {
                httpFactory.setDefaultRequestProperties(headers)
                it.setMediaItem(mediaItem)
            }
            it.prepare()
            it.play()
        }
    }

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

    fun resume() {
        player?.let {
            if (it.playbackState == Player.STATE_IDLE || it.playbackState == Player.STATE_ENDED) {
                if (currentUrl.isNotEmpty()) play(currentUrl, currentChannelId, currentChannelName)
            } else {
                it.play()
            }
        }
    }

    fun pause() {
        player?.pause()
    }

    fun togglePlayPause() {
        player?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                if (it.playbackState == Player.STATE_IDLE || it.playbackState == Player.STATE_ENDED) {
                    if (currentUrl.isNotEmpty()) play(currentUrl, currentChannelId, currentChannelName)
                } else {
                    it.play()
                }
            }
        }
    }

    fun isPlaying(): Boolean = player?.isPlaying == true
    fun getCurrentPosition(): Long = player?.currentPosition ?: 0L
    fun getCurrentChannelId(): Long = currentChannelId
    fun getPlaybackState(): Int = player?.playbackState ?: Player.STATE_IDLE

    fun release() {
        cancelSleepTimer()
        player?.let {
            it.removeListener(internalListener)
            it.release()
        }
        player = null
        currentUrl = ""
        currentChannelId = 0
        currentChannelName = ""
        listener = null
        currentPlayerView = null
    }

    fun setSleepTimerListener(l: SleepTimerListener?) {
        sleepTimerListener = l
    }

    fun startSleepTimerAt(targetHour: Int, targetMinute: Int) {
        cancelSleepTimer()
        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, targetHour)
            set(java.util.Calendar.MINUTE, targetMinute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            if (timeInMillis <= now.timeInMillis) add(java.util.Calendar.DAY_OF_YEAR, 1)
        }
        val totalMs = target.timeInMillis - now.timeInMillis
        if (totalMs <= 0) return
        targetTimeMs = target.timeInMillis
        remainingTimeMs = totalMs
        sleepTimer = object : CountDownTimer(totalMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeMs = millisUntilFinished
                sleepTimerListener?.onSleepTimerTick(millisUntilFinished)
            }
            override fun onFinish() {
                remainingTimeMs = 0
                targetTimeMs = 0
                pause()
                sleepTimerListener?.onSleepTimerFinish()
            }
        }.start()
    }

    fun cancelSleepTimer() {
        sleepTimer?.cancel()
        sleepTimer = null
        remainingTimeMs = 0
        targetTimeMs = 0
    }

    fun isSleepTimerActive(): Boolean = sleepTimer != null && remainingTimeMs > 0
    fun getSleepTimerRemainingMs(): Long = remainingTimeMs

    fun openSystemScreenCast(context: Context) {
        val actions = listOf(
            android.provider.Settings.ACTION_CAST_SETTINGS,
            "android.settings.WIFI_DISPLAY_SETTINGS",
            "android.settings.CONNECTED_DEVICE_SETTINGS",
            android.provider.Settings.ACTION_BLUETOOTH_SETTINGS,
            android.provider.Settings.ACTION_SETTINGS
        )
        val pm = context.packageManager
        for (action in actions) {
            val intent = Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent)
                return
            }
        }
    }

    private val internalListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            val stateName = when (state) {
                Player.STATE_IDLE -> "IDLE"
                Player.STATE_BUFFERING -> "BUFFERING"
                Player.STATE_READY -> "READY"
                Player.STATE_ENDED -> "ENDED"
                else -> "UNKNOWN($state)"
            }
            Log.d(TAG, "state=$stateName  ch=$currentChannelId  url=$currentUrl")
            listener?.onPlaybackStateChanged(state)
        }
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Log.d(TAG, "isPlaying=$isPlaying  ch=$currentChannelId")
            listener?.onIsPlayingChanged(isPlaying)
        }
        override fun onPlayerError(error: PlaybackException) {
            Log.e(
                TAG,
                "PLAYER ERROR  ch=$currentChannelId  name=$currentChannelName" +
                    "  code=${error.errorCode}  msg=${error.message}" +
                    "  cause=${error.cause?.javaClass?.simpleName}: ${error.cause?.message}" +
                    "  url=$currentUrl",
                error
            )
            listener?.onPlayerError(error)
        }
    }

    private const val TAG = "PlayerManager"
    private const val BROWSER_UA =
        "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 " +
        "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
}
