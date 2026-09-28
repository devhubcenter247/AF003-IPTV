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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.ads.wrapper.interstitial.InterstitialAdConfig
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.ChannelListItem
import com.dongbv.alf_03_iptv.ui.component.ChannelSearchBar
import com.dongbv.alf_03_iptv.ui.component.SleepTimerDialog
import com.dongbv.alf_03_iptv.viewmodel.PlayViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvDivider
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChannelHorActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.PLAY_CHANNEL_LANDSCAPE


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
        private var currentInstance: ChannelHorActivity? = null

        fun finishCurrentInstance() {
            currentInstance?.takeIf { !it.isFinishing }?.finish()
            currentInstance = null
        }

        fun newIntent(
            context: Context,
            id: Long,
            url: String,
            name: String,
            logo: String?,
            playlistId: Long
        ): Intent = Intent(context, ChannelHorActivity::class.java).apply {
            putExtra(PlayChannelActivity.EXTRA_CHANNEL_ID, id)
            putExtra(PlayChannelActivity.EXTRA_CHANNEL_URL, url)
            putExtra(PlayChannelActivity.EXTRA_CHANNEL_NAME, name)
            putExtra(PlayChannelActivity.EXTRA_CHANNEL_LOGO, logo)
            putExtra(PlayChannelActivity.EXTRA_PLAYLIST_ID, playlistId)
        }
    }

    override fun initialize() {
        currentInstance = this
        PlayChannelActivity.finishCurrentInstance()
        intent.let {
            channelId = it.getLongExtra(PlayChannelActivity.EXTRA_CHANNEL_ID, 0)
            channelName = it.getStringExtra(PlayChannelActivity.EXTRA_CHANNEL_NAME) ?: ""
            channelUrl = it.getStringExtra(PlayChannelActivity.EXTRA_CHANNEL_URL) ?: ""
            channelLogo = it.getStringExtra(PlayChannelActivity.EXTRA_CHANNEL_LOGO)
            playlistId = it.getLongExtra(PlayChannelActivity.EXTRA_PLAYLIST_ID, 0)
        }
        PlayerManager.initialize(this)
        viewModel.loadChannelsByPlaylist(playlistId)
        viewModel.setCurrentChannelById(channelId)
    }

    @Composable
    override fun ContentView() {
        val channels by viewModel.channels.collectAsStateWithLifecycle()
        val currentChannel by viewModel.currentChannel.collectAsStateWithLifecycle()
        val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
        val actions = remember(viewModel) {
            object : ChannelHorActions {
                override fun onPlayerViewReady(view: PlayerView) {
                    _activePlayerView = view
                    PlayerManager.attachToView(view)
                    val currentId = PlayerManager.getCurrentChannelId()
                    if (currentId != 0L && PlayerManager.getPlaybackState() != Player.STATE_IDLE) {
                        viewModel.setCurrentChannelById(currentId)
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

                override fun onMiniScreen() {
                    goBackToPortrait()
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

                override fun onCast() {
                    PlayerManager.openSystemScreenCast(this@ChannelHorActivity)
                }

                override fun onEnterPip() {
                    enterPipMode()
                }
            }
        }

        ChannelHorScreen(
            channels = channels,
            currentChannelId = currentChannel?.id ?: channelId,
            currentChannelName = currentChannel?.name ?: channelName,
            isFavorite = isFavorite,
            actions = actions
        )
    }

    private fun playChannel(channel: ChannelEntity) {
        channelId = channel.id
        channelUrl = channel.url
        channelName = channel.name
        channelLogo = channel.logo
        viewModel.setCurrentChannel(channel)
        PlayerManager.play(channel.url, channel.id, channel.name)
    }

    private fun goBackToPortrait() {
        isMinimizing = true
        _activePlayerView?.let { PlayerManager.detachFromView(it) }
        startActivity(
            PlayChannelActivity.newIntent(this, channelId, channelName, channelUrl, channelLogo, playlistId)
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

// ─── Fullscreen landscape UI ──────────────────────────────────────────────────

@Stable
interface ChannelHorActions {
    fun onPlayerViewReady(view: PlayerView)
    fun onPlayerViewDisposed(view: PlayerView)
    fun onBack()
    fun onMiniScreen()
    fun onPlayChannel(channel: ChannelEntity)
    fun onToggleFavorite()
    fun onPrevious()
    fun onNext()
    fun onSearch(query: String)
    fun onCast()
    fun onEnterPip()

    companion object {
        val None = object : ChannelHorActions {
            override fun onPlayerViewReady(view: PlayerView) = Unit
            override fun onPlayerViewDisposed(view: PlayerView) = Unit
            override fun onBack() = Unit
            override fun onMiniScreen() = Unit
            override fun onPlayChannel(channel: ChannelEntity) = Unit
            override fun onToggleFavorite() = Unit
            override fun onPrevious() = Unit
            override fun onNext() = Unit
            override fun onSearch(query: String) = Unit
            override fun onCast() = Unit
            override fun onEnterPip() = Unit
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
@Composable
fun ChannelHorScreen(
    channels: List<ChannelEntity>,
    currentChannelId: Long,
    currentChannelName: String,
    isFavorite: Boolean,
    actions: ChannelHorActions = ChannelHorActions.None,
) {
    var isPlaying by remember { mutableStateOf(PlayerManager.isPlaying()) }
    var isBuffering by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }
    var isLocked by remember { mutableStateOf(false) }
    var showChannelList by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var playbackErrorSequence by remember { mutableLongStateOf(0L) }
    var activePlaybackErrorId by remember { mutableStateOf<Long?>(null) }
    var isSleepTimerActive by remember { mutableStateOf(PlayerManager.isSleepTimerActive()) }
    var sleepTimerRemainingMs by remember { mutableStateOf(PlayerManager.getSleepTimerRemainingMs()) }
    val playerViewRef = remember { mutableStateOf<PlayerView?>(null) }

    DisposableEffect(Unit) {
        onDispose { playerViewRef.value?.let { actions.onPlayerViewDisposed(it) } }
    }

    LaunchedEffect(showControls, isPlaying, isLocked) {
        if (showControls && isPlaying && !isLocked) {
            delay(4_000.milliseconds)
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
                if (activePlaybackErrorId == null) {
                    playbackErrorSequence += 1
                    activePlaybackErrorId = playbackErrorSequence
                }
            }
        }
        PlayerManager.setListener(listener)
        onDispose { PlayerManager.setListener(null) }
    }

    DisposableEffect(Unit) {
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
        onDispose { PlayerManager.setSleepTimerListener(null) }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (!isLocked) {
                    showControls = !showControls
                    if (showChannelList && !showControls) showChannelList = false
                }
            }
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
                    actions.onPlayerViewReady(it)
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
                    .background(Color.Black.copy(alpha = 0.40f))
            ) {
               
                Row(
                    Modifier
                        .fillMaxWidth()
                        .statusBarPadding()
                        .padding(horizontal = 16.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.Sdp)
                            .background(white.opacity(8), CircleShape)
                            .onClick(onClick = actions::onBack),
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
                    Spacer(Modifier.width(12.Sdp))
                   
                    PlayerCtrlButton(
                        iconRes = R.drawable.ic_app_menu,
                        onClick = { showChannelList = !showChannelList }
                    )
                }

               
                Row(
                    Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(32.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_skip_prev, size = 36.Sdp, onClick = actions::onPrevious)
                    PlayerCtrlButton(
                        iconRes = if (isPlaying) R.drawable.ic_app_pause else R.drawable.ic_app_play,
                        size = 48.Sdp,
                        onClick = { PlayerManager.togglePlayPause() }
                    )
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_skip_next, size = 36.Sdp, onClick = actions::onNext)
                }

               
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(horizontal = 16.Sdp, vertical = 8.Sdp),
                    horizontalArrangement = Arrangement.spacedBy(8.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerCtrlButton(
                        iconRes = if (isFavorite) R.drawable.ic_app_favorite_minus
                                  else R.drawable.ic_app_favorite_add,
                        tint = if (isFavorite) IptvAccent else Color.White,
                        onClick = actions::onToggleFavorite
                    )
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_minimize, onClick = actions::onMiniScreen)
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_cast, onClick = actions::onCast)
                    PlayerCtrlButton(iconRes = R.drawable.ic_app_timer, onClick = { showSleepTimerDialog = true })
                    PlayerCtrlButton(
                        iconRes = R.drawable.ic_app_lock,
                        onClick = { isLocked = true; showControls = false }
                    )
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
                    ) { /* swallow */ },
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    Modifier
                        .padding(end = 16.Sdp)
                        .size(48.Sdp)
                        .background(Color.Black.copy(0.5f), CircleShape)
                        .clickable { isLocked = false; showControls = true },
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

       
        AnimatedVisibility(
            visible = showChannelList,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it }),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .fillMaxWidth(0.32f)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(IptvBgPrimary.copy(alpha = 0.95f))
            ) {
                Text(
                    text = "Channels",
                    style = headline600(fontSize = 12.Ssp, color = IptvTextPrimary),
                    modifier = Modifier.padding(horizontal = 12.Sdp, vertical = 8.Sdp)
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(channels, key = { it.id }) { channel ->
                        ChannelListItem(
                            channel = channel,
                            isTablet = true,
                            isSelected = channel.id == currentChannelId,
                            onClick = {
                                actions.onPlayChannel(channel)
                                showChannelList = false
                            }
                        )
                    }
                }
            }
        }
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HorChannelListItemPreview() {
    AppViewTheme {
        Column(modifier = Modifier.width(300.Sdp)) {
            ChannelListItem(
                channel = ChannelEntity.fakeSingle(),
                isTablet = true,
                isSelected = true,
                onClick = {}
            )
            ChannelListItem(
                channel = ChannelEntity.fakeSingle().copy(id = 2, name = "Unselected Channel"),
                isTablet = true,
                isSelected = false,
                onClick = {}
            )
        }
    }
}

@ResponsivePreview
@Composable
private fun ChannelHorScreenPreview() {
    val channels = ChannelEntity.fakeList()
    val currentChannel = channels.first()

    AppViewTheme {
        ChannelHorScreen(
            channels = channels,
            currentChannelId = currentChannel.id,
            currentChannelName = currentChannel.name,
            isFavorite = true,
            actions = ChannelHorActions.None
        )
    }
}
