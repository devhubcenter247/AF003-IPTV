package com.dongbv.alf_03_iptv.ui.main.play

import com.dongbv.alf_03_iptv.analytics.AppScreen
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.lib.ads.gma.ads.billing.AppPurchase
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.ads.wrapper.interstitial.InterstitialAdConfig
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.ChannelListItem
import com.dongbv.alf_03_iptv.ui.component.ChannelSearchBar
import com.dongbv.alf_03_iptv.ui.component.SleepTimerDialog
import com.dongbv.alf_03_iptv.ui.component.backgroundState
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.viewmodel.PlayViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvSuccess
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.milliseconds

class PlayChannelActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.PLAY_CHANNEL


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
    override fun ContentView() {
        val channels by viewModel.channels.collectAsStateWithLifecycle()
        val currentChannel by viewModel.currentChannel.collectAsStateWithLifecycle()
        val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
        val actions = remember(viewModel) {
            object : PlayChannelActions {
                override fun onPlayerViewReady(view: PlayerView) {
                    _activePlayerView = view
                    PlayerManager.attachToView(view)
                    if (PlayerManager.getCurrentChannelId() == channelId &&
                        PlayerManager.getPlaybackState() != Player.STATE_IDLE
                    ) {

                    } else {
                        PlayerManager.play(channelUrl, channelId, channelName)
                    }
                }

                override fun onPlayerViewDisposed(view: PlayerView) {
                    PlayerManager.detachFromView(view)
                    if (_activePlayerView == view) _activePlayerView = null
                }

                override fun onBack() {
                    InterstitialAdConfig.recordPlayerClosed()
                    finish()
                }

                override fun onPlayChannel(channel: ChannelEntity) {
                    playChannel(channel)
                }

                override fun onToggleFavorite() {
                    viewModel.toggleFavorite()
                }

                override fun onPrevious() {
                    viewModel.getPreviousChannel()?.let { playChannel(it) }
                }

                override fun onNext() {
                    viewModel.getNextChannel()?.let { playChannel(it) }
                }

                override fun onSearch(query: String) {
                    viewModel.searchChannels(query)
                }

                override fun onFullscreen() {
                    openFullscreen()
                }

                override fun onCast() {
                    PlayerManager.openSystemScreenCast(this@PlayChannelActivity)
                }

                override fun onEnterPip() {
                    enterPipMode()
                }
            }
        }

        PlayChannelScreen(
            channels = channels,
            currentChannelId = currentChannel?.id ?: channelId,
            currentChannelName = currentChannel?.name ?: channelName,
            isFavorite = isFavorite,
            actions = actions,
            footer = { PlayChannelFooterBanner() }
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
            ChannelHorActivity.newIntent(
                this,
                channelId,
                channelUrl,
                channelName,
                channelLogo,
                playlistId
            )
        )
        finish()
    }

    private fun enterPipMode() {
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

@Stable
interface PlayChannelActions {
    fun onPlayerViewReady(view: PlayerView)
    fun onPlayerViewDisposed(view: PlayerView)
    fun onBack()
    fun onPlayChannel(channel: ChannelEntity)
    fun onToggleFavorite()
    fun onPrevious()
    fun onNext()
    fun onSearch(query: String)
    fun onFullscreen()
    fun onCast()
    fun onEnterPip()

    companion object {
        val None = object : PlayChannelActions {
            override fun onPlayerViewReady(view: PlayerView) = Unit
            override fun onPlayerViewDisposed(view: PlayerView) = Unit
            override fun onBack() = Unit
            override fun onPlayChannel(channel: ChannelEntity) = Unit
            override fun onToggleFavorite() = Unit
            override fun onPrevious() = Unit
            override fun onNext() = Unit
            override fun onSearch(query: String) = Unit
            override fun onFullscreen() = Unit
            override fun onCast() = Unit
            override fun onEnterPip() = Unit
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
@Composable
fun PlayChannelScreen(
    channels: List<ChannelEntity>,
    currentChannelId: Long,
    currentChannelName: String,
    isFavorite: Boolean,
    actions: PlayChannelActions = PlayChannelActions.None,
    footer: @Composable () -> Unit = {},
) {
    val showChannelNativeAds = !LocalInspectionMode.current &&
        NativePlacement.NativePlayList.canShowAds &&
        NativePlacement.NativePlayList.listId.any { it.isNotBlank() } &&
        !AppPurchase.getInstance().isPurchased()
    var isPlaying by remember { mutableStateOf(PlayerManager.isPlaying()) }
    var isBuffering by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }
    var isLocked by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var playbackErrorSequence by remember { mutableLongStateOf(0L) }
    var activePlaybackErrorId by remember { mutableStateOf<Long?>(null) }
    var isSleepTimerActive by remember { mutableStateOf(PlayerManager.isSleepTimerActive()) }
    var sleepTimerRemainingMs by remember { mutableStateOf(PlayerManager.getSleepTimerRemainingMs()) }


    LaunchedEffect(showControls, isPlaying, isLocked) {
        if (showControls && isPlaying && !isLocked) {
            delay(4_000.milliseconds)
            showControls = false
        }
    }


    LifecycleResumeEffect(Unit) {
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
                if (activePlaybackErrorId == null) {
                    playbackErrorSequence += 1
                    activePlaybackErrorId = playbackErrorSequence
                }
            }
        }
        PlayerManager.setListener(listener)
        onPauseOrDispose { PlayerManager.setListener(null) }
    }

    LifecycleResumeEffect(Unit) {
        val sleepListener = object : PlayerManager.SleepTimerListener {
            override fun onSleepTimerTick(remainingMs: Long) {
                sleepTimerRemainingMs = remainingMs
                isSleepTimerActive = true
            }

            override fun onSleepTimerFinish() {
                sleepTimerRemainingMs = 0
                isSleepTimerActive = false
            }
        }
        PlayerManager.setSleepTimerListener(sleepListener)
        isSleepTimerActive = PlayerManager.isSleepTimerActive()
        sleepTimerRemainingMs = PlayerManager.getSleepTimerRemainingMs()
        onPauseOrDispose { PlayerManager.setSleepTimerListener(null) }
    }

    Column(Modifier.fillMaxSize()) {
        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .backgroundResource(R.drawable.img_bg_app)
        ) {
            val isTablet = maxWidth >= 600.dp

            if (isTablet) {
                Row(Modifier.fillMaxSize()) {
                    PlayerSurface(
                        modifier = Modifier
                            .weight(0.68f)
                            .fillMaxHeight(),
                        showControls = showControls,
                        isLocked = isLocked,
                        isPlaying = isPlaying,
                        isBuffering = isBuffering,
                        isFavorite = isFavorite,
                        currentChannelName = currentChannelName,
                        onToggleControls = { if (!isLocked) showControls = !showControls },
                        onBack = actions::onBack,
                        onPrevious = actions::onPrevious,
                        onNext = actions::onNext,
                        onToggleFavorite = actions::onToggleFavorite,
                        onFullscreen = actions::onFullscreen,
                        onCast = actions::onCast,
                        onOpenSleepTimer = { showSleepTimerDialog = true },
                        onLock = { isLocked = true; showControls = false },
                        onUnlock = { isLocked = false; showControls = true },
                        onPlayerViewReady = actions::onPlayerViewReady,
                        onPlayerViewDisposed = actions::onPlayerViewDisposed
                    )

                    Column(
                        modifier = Modifier
                            .weight(0.375f)
                            .fillMaxHeight()

                    ) {
                        ChannelSearchBar(
                            query = searchQuery,
                            onQueryChanged = {
                                searchQuery = it
                                actions.onSearch(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.Sdp, vertical = 8.Sdp)
                        )

                        LazyColumn(
                            state = rememberLazyListState(),
                            contentPadding = PaddingValues(horizontal = 12.Sdp, vertical = 8.Sdp),
                            verticalArrangement = Arrangement.spacedBy(8.Sdp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            playChannelItems(channels, showChannelNativeAds) { channel ->
                                ChannelListItem(
                                    channel = channel,
                                    isTablet = true,
                                    isSelected = channel.id == currentChannelId,
                                    onClick = { actions.onPlayChannel(channel) }
                                )
                            }
                        }
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxSize()

                ) {
                    PlayerSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.325f),
                        showControls = showControls,
                        isLocked = isLocked,
                        isPlaying = isPlaying,
                        isBuffering = isBuffering,
                        isFavorite = isFavorite,
                        currentChannelName = currentChannelName,
                        onToggleControls = { if (!isLocked) showControls = !showControls },
                        onBack = actions::onBack,
                        onPrevious = actions::onPrevious,
                        onNext = actions::onNext,
                        onToggleFavorite = actions::onToggleFavorite,
                        onFullscreen = actions::onFullscreen,
                        onCast = actions::onCast,
                        onOpenSleepTimer = { showSleepTimerDialog = true },
                        onLock = { isLocked = true; showControls = false },
                        onUnlock = { isLocked = false; showControls = true },
                        onPlayerViewReady = actions::onPlayerViewReady,
                        onPlayerViewDisposed = actions::onPlayerViewDisposed
                    )

                    ChannelSearchBar(
                        query = searchQuery,
                        onQueryChanged = {
                            searchQuery = it
                            actions.onSearch(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.Sdp, vertical = 8.Sdp)
                    )

                    LazyColumn(
                        state = rememberLazyListState(),
                        contentPadding = PaddingValues( bottom = 8.Sdp),
                        verticalArrangement = Arrangement.spacedBy(4.Sdp),
                        modifier = Modifier
                            .fillMaxSize()

                    ) {
                        playChannelItems(channels, showChannelNativeAds) { channel ->
                            ChannelListItem(
                                channel = channel,
                                isSelected = channel.id == currentChannelId,
                                onClick = { actions.onPlayChannel(channel) }
                            )
                        }
                    }
                }
            }
        }
        footer()
    }

    SleepTimerDialog(
        isShow = showSleepTimerDialog,
        isActive = isSleepTimerActive,
        remainingMs = sleepTimerRemainingMs,
        onSetTimer = { hour, minute ->
            PlayerManager.startSleepTimerAt(hour, minute)
            isSleepTimerActive = true
            sleepTimerRemainingMs = PlayerManager.getSleepTimerRemainingMs()
            showSleepTimerDialog = false
        },
        onCancelTimer = {
            PlayerManager.cancelSleepTimer()
            isSleepTimerActive = false
            sleepTimerRemainingMs = 0
            showSleepTimerDialog = false
        },
        onDismiss = { showSleepTimerDialog = false }
    )

    PlaybackErrorDialog(
        errorEventId = activePlaybackErrorId,
        onPlayNext = { handledErrorId ->
            if (activePlaybackErrorId == handledErrorId) {
                activePlaybackErrorId = null
                actions.onNext()
            }
        },
    )
}

@Composable
private fun PlayerSurface(
    modifier: Modifier,
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
    onOpenSleepTimer: () -> Unit,
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
                    .background(IptvBgPrimary.opacity(0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarPadding()
                        .padding(horizontal = 8.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.Sdp)
                            .background(white.opacity(8), CircleShape)
                            .onClick(onClick = onBack),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_app_back),
                            contentDescription = null,
                            tint = IptvTextPrimary,
                            modifier = Modifier.size(14.Sdp)
                        )
                    }
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
                    PlayerCtrlButton(
                        iconRes = R.drawable.ic_app_skip_prev,
                        size = 32.Sdp,
                        onClick = onPrevious
                    )
                    PlayerCtrlButton(
                        iconRes = if (isPlaying) R.drawable.ic_app_pause else R.drawable.ic_app_play,
                        size = 48.Sdp,
                        onClick = { PlayerManager.togglePlayPause() }
                    )
                    PlayerCtrlButton(
                        iconRes = R.drawable.ic_app_skip_next,
                        size = 32.Sdp,
                        onClick = onNext
                    )
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
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_timer, onClick = onOpenSleepTimer)
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

@Composable
fun LiveBadge() {
    Box(
        modifier = Modifier
            .neonState(true, colors = listOf(IptvSuccess, IptvSuccess))
            .backgroundState(enableColors = listOf(IptvSuccess, IptvSuccess), enabled = true)
            .padding(horizontal = 6.Sdp, vertical = 2.Sdp)
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

@ResponsivePreview
@Composable
fun PlayChannelScreenPreview() {
    val channels = ChannelEntity.fakeList()
    AppViewTheme {
        PlayChannelScreen(
            channels = channels,
            currentChannelId = channels.first().id,
            currentChannelName = channels.first().name,
            isFavorite = true,
            actions = PlayChannelActions.None
        )
    }
}

@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun PlayChannelScreenTabletPreview() {
    val channels = ChannelEntity.fakeList()
    AppViewTheme {
        PlayChannelScreen(
            channels = channels,
            currentChannelId = channels.first().id,
            currentChannelName = channels.first().name,
            isFavorite = true,
            actions = PlayChannelActions.None
        )
    }
}
