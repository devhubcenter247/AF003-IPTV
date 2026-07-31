package com.dongbv.alf_03_iptv.ui.main.play

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.util.Rational
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.viewmodel.PlayViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvBgSecondary
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvSuccess
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayChannelActivity : BaseActivity() {

    private val viewModel: PlayViewModel by viewModel()

    private var channelId: Long = 0
    private var channelName: String = ""
    private var channelUrl: String = ""
    private var channelLogo: String? = null
    private var playlistId: Long = 0
    private var isMinimizing = false
    private var isInPipMode = false
    private var isEnteringPipMode = false
    private var _activePlayerView: PlayerView? = null

    companion object {
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
        const val EXTRA_CHANNEL_NAME = "extra_channel_name"
        const val EXTRA_CHANNEL_URL = "extra_channel_url"
        const val EXTRA_CHANNEL_LOGO = "extra_channel_logo"
        const val EXTRA_PLAYLIST_ID = "extra_playlist_id"
        private var currentInstance: PlayChannelActivity? = null

        fun finishCurrentInstance() {
            currentInstance?.takeIf { !it.isFinishing }?.finish()
            currentInstance = null
        }

        fun newIntent(
            context: Context,
            id: Long,
            name: String,
            url: String,
            logo: String?,
            playlistId: Long
        ): Intent = Intent(context, PlayChannelActivity::class.java).apply {
            putExtra(EXTRA_CHANNEL_ID, id)
            putExtra(EXTRA_CHANNEL_NAME, name)
            putExtra(EXTRA_CHANNEL_URL, url)
            putExtra(EXTRA_CHANNEL_LOGO, logo)
            putExtra(EXTRA_PLAYLIST_ID, playlistId)
        }
    }

    override fun initialize() {
        currentInstance = this
        ChannelHorActivity.finishCurrentInstance()
        intent.let {
            channelId = it.getLongExtra(EXTRA_CHANNEL_ID, 0)
            channelName = it.getStringExtra(EXTRA_CHANNEL_NAME) ?: ""
            channelUrl = it.getStringExtra(EXTRA_CHANNEL_URL) ?: ""
            channelLogo = it.getStringExtra(EXTRA_CHANNEL_LOGO)
            playlistId = it.getLongExtra(EXTRA_PLAYLIST_ID, 0)
        }
        PlayerManager.initialize(this)
        viewModel.loadChannelsByPlaylist(playlistId)
        viewModel.setCurrentChannelById(channelId)
        viewModel.preloadCheck(
            com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity(
                id = channelId, playlistId = playlistId,
                name = channelName, url = channelUrl, logo = channelLogo
            )
        )
    }

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        val channels by viewModel.channels.collectAsStateWithLifecycle()
        val currentChannel by viewModel.currentChannel.collectAsStateWithLifecycle()
        val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

        PlayChannelScreen(
            statusBarHeight = statusBarHeight,
            channels = channels,
            currentChannelId = currentChannel?.id ?: channelId,
            currentChannelName = currentChannel?.name ?: channelName,
            isFavorite = isFavorite,
            onPlayerViewReady = { view ->
                _activePlayerView = view
                PlayerManager.attachToView(view)
                if (PlayerManager.getCurrentChannelId() == channelId &&
                    PlayerManager.getPlaybackState() != Player.STATE_IDLE
                ) {
                   
                } else {
                    PlayerManager.play(channelUrl, channelId, channelName)
                }
            },
            onPlayerViewDisposed = { view ->
                PlayerManager.detachFromView(view)
                if (_activePlayerView == view) _activePlayerView = null
            },
            onBack = { finish() },
            onPlayChannel = { channel -> playChannel(channel) },
            onToggleFavorite = { viewModel.toggleFavorite() },
            onPrevious = { viewModel.getPreviousChannel()?.let { playChannel(it) } },
            onNext = { viewModel.getNextChannel()?.let { playChannel(it) } },
            onSearch = { viewModel.searchChannels(it) },
            onFullscreen = { openFullscreen() },
            onCast = { PlayerManager.openSystemScreenCast(this@PlayChannelActivity) },
            onEnterPip = { enterPipMode() }
        )
    }

    private fun playChannel(channel: ChannelEntity) {
        channelId = channel.id
        channelUrl = channel.url
        channelName = channel.name
        channelLogo = channel.logo
        viewModel.setCurrentChannel(channel)
        viewModel.preloadCheck(channel)
        PlayerManager.play(channel.url, channel.id, channel.name)
    }

    private fun openFullscreen() {
        isMinimizing = true
        _activePlayerView?.let { PlayerManager.detachFromView(it) }
        startActivity(
            ChannelHorActivity.newIntent(this, channelId, channelUrl, channelName, channelLogo, playlistId)
        )
        finish()
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            isEnteringPipMode = true
            val rect = Rect()
            _activePlayerView?.getGlobalVisibleRect(rect)
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(16, 9))
                .setSourceRectHint(rect)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        setAutoEnterEnabled(false)
                    }
                }
                .build()
            enterPictureInPictureMode(params)
        }
    }

    override fun onPictureInPictureModeChanged(isInPipMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPipMode, newConfig)
        this.isInPipMode = isInPipMode
        isEnteringPipMode = false
    }

    override fun onResume() {
        super.onResume()
        _activePlayerView?.let { PlayerManager.attachToView(it) }
        if (!isInPipMode) PlayerManager.resume()
    }

    override fun onPause() {
        super.onPause()
        if (!isInPipMode && !isEnteringPipMode) PlayerManager.pause()
    }

    override fun onStop() {
        super.onStop()
        if (isInPipMode && !isChangingConfigurations && !isMinimizing) {
            _activePlayerView?.let {
                if (PlayerManager.isCurrentView(it)) PlayerManager.release()
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (currentInstance == this) currentInstance = null
        if (isFinishing && !isInPipMode && !isMinimizing) {
            _activePlayerView?.let {
                if (PlayerManager.isCurrentView(it)) {
                    viewModel.updateWatchProgress(PlayerManager.getCurrentPosition())
                    PlayerManager.release()
                }
            }
        }
    }
}

// ─── Composable UI ────────────────────────────────────────────────────────────

@SuppressLint("ClickableViewAccessibility")
@Composable
fun PlayChannelScreen(
    statusBarHeight: Dp,
    channels: List<ChannelEntity>,
    currentChannelId: Long,
    currentChannelName: String,
    isFavorite: Boolean,
    onPlayerViewReady: (PlayerView) -> Unit,
    onPlayerViewDisposed: (PlayerView) -> Unit,
    onBack: () -> Unit,
    onPlayChannel: (ChannelEntity) -> Unit,
    onToggleFavorite: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSearch: (String) -> Unit,
    onFullscreen: () -> Unit,
    onCast: () -> Unit,
    onEnterPip: () -> Unit,
) {
    var isPlaying by remember { mutableStateOf(PlayerManager.isPlaying()) }
    var isBuffering by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }
    var isLocked by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

   
    LaunchedEffect(showControls, isPlaying, isLocked) {
        if (showControls && isPlaying && !isLocked) {
            delay(4_000)
            showControls = false
        }
    }

   
    DisposableEffect(Unit) {
        val listener = object : PlayerManager.PlayerListener {
            override fun onPlaybackStateChanged(state: Int) {
                isBuffering = state == Player.STATE_BUFFERING
            }
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
                if (!playing) showControls = true
            }
            override fun onPlayerError(error: PlaybackException) {
                isBuffering = false
                showControls = true
            }
        }
        PlayerManager.setListener(listener)
        onDispose { PlayerManager.setListener(null) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
       
        PlayerSurface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.28f),
            statusBarHeight = statusBarHeight,
            showControls = showControls,
            isLocked = isLocked,
            isPlaying = isPlaying,
            isBuffering = isBuffering,
            isFavorite = isFavorite,
            currentChannelName = currentChannelName,
            onToggleControls = { if (!isLocked) showControls = !showControls },
            onBack = onBack,
            onPrevious = onPrevious,
            onNext = onNext,
            onToggleFavorite = onToggleFavorite,
            onFullscreen = onFullscreen,
            onCast = onCast,
            onLock = { isLocked = true; showControls = false },
            onUnlock = { isLocked = false; showControls = true },
            onPlayerViewReady = onPlayerViewReady,
            onPlayerViewDisposed = onPlayerViewDisposed
        )

       
        SearchBar(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                onSearch(it)
            }
        )

       
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxSize()
                .background(IptvBgPrimary)
        ) {
            items(channels, key = { it.id }) { channel ->
                ChannelListItem(
                    channel = channel,
                    isSelected = channel.id == currentChannelId,
                    onClick = { onPlayChannel(channel) }
                )
            }
        }
    }
}

@Composable
private fun PlayerSurface(
    modifier: Modifier,
    statusBarHeight: Dp,
    showControls: Boolean,
    isLocked: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    isFavorite: Boolean,
    currentChannelName: String,
    onToggleControls: () -> Unit,
    onBack: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onToggleFavorite: () -> Unit,
    onFullscreen: () -> Unit,
    onCast: () -> Unit,
    onLock: () -> Unit,
    onUnlock: () -> Unit,
    onPlayerViewReady: (PlayerView) -> Unit,
    onPlayerViewDisposed: (PlayerView) -> Unit,
) {
    val playerViewRef = remember { mutableStateOf<PlayerView?>(null) }
    DisposableEffect(Unit) {
        onDispose { playerViewRef.value?.let { onPlayerViewDisposed(it) } }
    }

    Box(
        modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onToggleControls
            )
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }.also {
                    playerViewRef.value = it
                    onPlayerViewReady(it)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        AnimatedVisibility(
            visible = showControls && !isLocked,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = statusBarHeight)
                        .padding(horizontal = 8.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_back, onClick = onBack)
                    Spacer(Modifier.width(8.Sdp))
                    Text(
                        text = currentChannelName,
                        style = headline600(fontSize = 12.Ssp, color = IptvTextPrimary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.Sdp))
                    LiveBadge()
                }

                Row(
                    Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(24.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_skip_prev, size = 32.Sdp, onClick = onPrevious)
                    PlayerCtrlButton(
                        iconRes = if (isPlaying) R.drawable.ic_app_pause else R.drawable.ic_app_play,
                        size = 48.Sdp,
                        onClick = { PlayerManager.togglePlayPause() }
                    )
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_skip_next, size = 32.Sdp, onClick = onNext)
                }

                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(horizontal = 8.Sdp, vertical = 4.Sdp),
                    horizontalArrangement = Arrangement.spacedBy(4.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerCtrlButton(
                        iconRes = if (isFavorite) R.drawable.ic_app_favorite_minus
                                  else R.drawable.ic_app_favorite_add,
                        tint = if (isFavorite) IptvAccent else Color.White,
                        onClick = onToggleFavorite
                    )
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_fullscreen, onClick = onFullscreen)
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_cast, onClick = onCast)
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_timer, onClick = { })
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_lock, onClick = onLock)
                }
            }
        }

        if (isLocked) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { },
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    Modifier
                        .padding(end = 12.Sdp)
                        .size(36.Sdp)
                        .background(Color.Black.copy(0.5f), CircleShape)
                        .clickable(onClick = onUnlock),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_app_unlock),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.Sdp)
                    )
                }
            }
        }

        if (isBuffering) {
            CircularProgressIndicator(
                color = IptvAccent,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

// ─── Shared composables ───────────────────────────────────────────────────────

@Composable
fun LiveBadge() {
    Box(
        modifier = Modifier
            .background(IptvSuccess, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(R.string.live),
            style = headline700(fontSize = 10.Ssp, color = Color.White)
        )
    }
}

@Composable
fun PlayerCtrlButton(
    iconRes: Int,
    size: Dp = 28.dp,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .size(size)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.fillMaxSize(0.8f)
        )
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(IptvBgSecondary)
            .padding(horizontal = 12.Sdp, vertical = 8.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_app_search),
            contentDescription = null,
            tint = IptvTextSecondary,
            modifier = Modifier.size(20.Sdp)
        )
        Spacer(Modifier.width(8.Sdp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            decorationBox = { inner ->
                if (query.isEmpty()) {
                    Text("Search channels…", style = headline400(fontSize = 12.Ssp, color = IptvTextSecondary))
                }
                inner()
            },
            textStyle = headline400(fontSize = 12.Ssp, color = IptvTextPrimary)
        )
        if (query.isNotEmpty()) {
            Spacer(Modifier.width(8.Sdp))
            Box(
                Modifier
                    .size(20.Sdp)
                    .clickable { onQueryChange("") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_app_delete),
                    contentDescription = null,
                    tint = IptvTextSecondary,
                    modifier = Modifier.size(16.Sdp)
                )
            }
        }
    }
}

@Composable
fun ChannelListItem(
    channel: ChannelEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) IptvAccent.copy(alpha = 0.15f) else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.Sdp, vertical = 8.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
       
        Box(
            Modifier
                .size(48.Sdp)
                .clip(RoundedCornerShape(8.Sdp))
                .background(IptvBgSecondary),
            contentAlignment = Alignment.Center
        ) {
            if (!channel.logo.isNullOrBlank()) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_channel),
                    contentDescription = null,
                    tint = IptvTextSecondary,
                    modifier = Modifier.size(24.Sdp)
                )
            }
        }

        Spacer(Modifier.width(12.Sdp))

        Column(Modifier.weight(1f)) {
            Text(
                text = channel.name,
                style = headline600(
                    fontSize = 12.Ssp,
                    color = if (isSelected) IptvAccent else IptvTextPrimary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!channel.groupName.isNullOrBlank()) {
                Text(
                    text = channel.groupName,
                    style = headline400(fontSize = 10.Ssp, color = IptvTextSecondary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (isSelected) {
            Box(
                Modifier
                    .size(8.dp)
                    .background(IptvAccent, CircleShape)
            )
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.Sdp)
            .height(0.5.dp)
            .background(IptvDivider)
    )
}

