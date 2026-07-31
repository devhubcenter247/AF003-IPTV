package com.dongbv.alf_03_iptv.ui.main.watch

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.media3.common.Player
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.GradientCircularProgressIndicator
import com.dongbv.alf_03_iptv.style.PhonePreview
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.viewmodel.WatchNowViewModel
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvSuccess
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.IptvTextSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("UnsafeOptInUsageError")
class WatchNowActivity : BaseActivity() {

    private val viewModel: WatchNowViewModel by viewModel()

    override fun initialize() {}

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        val channels by viewModel.channels.collectAsStateWithLifecycle()
        val isMuted by viewModel.isMuted.collectAsStateWithLifecycle()
        val pagePlayerMap by viewModel.pagePlayerMap.collectAsStateWithLifecycle()

        WatchNowContent(
            channels = channels,
            isMuted = isMuted,
            pagePlayerMap = pagePlayerMap,
            autoNextEvent = viewModel.autoNextEvent,
            statusBarHeight = statusBarHeight,
            onBack = { finish() },
            onToggleMute = { viewModel.toggleMute() },
            onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
            onPageSelected = { page -> viewModel.updateWindow(page) }
        )
    }
}

@Composable
fun WatchNowContent(
    channels: List<ChannelEntity>,
    isMuted: Boolean,
    pagePlayerMap: Map<Int, ExoPlayer>,
    autoNextEvent: SharedFlow<Int>? = null,
    statusBarHeight: Dp,
    onBack: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onPageSelected: (Int) -> Unit,
) {
    if (channels.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().background(IptvBgPrimary),
            contentAlignment = Alignment.Center
        ) {
            GradientCircularProgressIndicator()
        }
        return
    }

    val pagerState = rememberPagerState { channels.size }

    // Advance to next page on stream error
    LaunchedEffect(autoNextEvent) {
        autoNextEvent?.collect { nextPage ->
            if (nextPage < channels.size) {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page -> onPageSelected(page) }
    }

    VerticalPager(
        state = pagerState,
        // Keep 2 pages beyond viewport so players [i-2..i+2] stay in composition
        beyondViewportPageCount = 2,
        modifier = Modifier.fillMaxSize().background(Color.Black),
    ) { page ->
        val channel = channels[page]
        ChannelReelPage(
            channel = channel,
            // null when page is outside the 5-slot cache window
            player = pagePlayerMap[page],
            isMuted = isMuted,
            statusBarHeight = statusBarHeight,
            onBack = onBack,
            onToggleMute = onToggleMute,
            onToggleFavorite = { onToggleFavorite(channel.id) },
        )
    }
}

@Composable
private fun ChannelReelPage(
    channel: ChannelEntity,
    player: ExoPlayer?,   // null → outside the 5-slot cache window
    isMuted: Boolean,
    statusBarHeight: Dp,
    onBack: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    var showControls by remember { mutableStateOf(true) }
    var isFavorite by remember(channel.id) { mutableStateOf(channel.isFavorite) }
    var isBuffering by remember(player) { mutableStateOf(player?.playbackState == Player.STATE_BUFFERING) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = playbackState == Player.STATE_BUFFERING
            }
        }
        player?.addListener(listener)
        onDispose { player?.removeListener(listener) }
    }

    LaunchedEffect(showControls) {
        if (showControls) {
            delay(3_000)
            showControls = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onClick { showControls = !showControls }
    ) {
        // Each page owns its own pooled player — just attach it to the surface.
        // AndroidView update block handles player reassignment if the pool recycles.
        if (player != null) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = false
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { view ->
                    if (view.player !== player) view.player = player
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Page is beyond the ±2 window — show logo thumbnail as placeholder
            if (!channel.logo.isNullOrBlank()) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)))
        }

        // Buffering indicator
        if (isBuffering) {
            GradientCircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Bottom scrim
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                    )
                )
        )

        // Top scrim
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                    )
                )
        )

        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize()) {

                // Top bar: back + LIVE badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = statusBarHeight)
                        .padding(horizontal = 16.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundIconButton(iconRes = R.drawable.ic_app_back, onClick = onBack)
                    Spacer(Modifier.weight(1f))
                    LiveBadge()
                }

                // Right-side action buttons
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.Sdp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.Sdp)
                ) {
                    ChannelAvatar(logoUrl = channel.logo)
                    ActionButton(
                        iconRes = if (isFavorite) R.drawable.ic_app_favorite_minus
                                  else R.drawable.ic_app_favorite_add,
                        tint = if (isFavorite) IptvAccent else IptvTextPrimary,
                        label = if (isFavorite) "Saved" else "Save",
                        onClick = {
                            isFavorite = !isFavorite
                            onToggleFavorite()
                        }
                    )
                    // No mute/volume drawable exists yet in res/drawable — question/info icons
                    // are a placeholder stand-in until a proper speaker asset is added.
                    ActionButton(
                        iconRes = if (isMuted) R.drawable.ic_app_question else R.drawable.ic_app_info,
                        tint = IptvTextPrimary,
                        label = if (isMuted) "Unmute" else "Mute",
                        onClick = onToggleMute
                    )
                }

                // Bottom channel info
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(0.75f)
                        .padding(start = 16.Sdp, bottom = 32.Sdp)
                ) {
                    if (!channel.groupName.isNullOrBlank()) {
                        Text(
                            text = channel.groupName,
                            style = headline400(fontSize = 10.Ssp, color = IptvTextSecondary),
                        )
                        Spacer(Modifier.height(4.Sdp))
                    }
                    Text(
                        text = channel.name,
                        style = headline700(fontSize = 14.Ssp, color = IptvTextPrimary),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(8.Sdp))
                    Text(
                        text = stringResource(R.string.watch_now),
                        style = headline600(fontSize = 12.Ssp, color = IptvAccent),
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveBadge() {
    Box(
        modifier = Modifier
            .background(IptvSuccess, RoundedCornerShape(4.Sdp))
            .padding(horizontal = 8.Sdp, vertical = 4.Sdp)
    ) {
        Text(
            text = stringResource(R.string.live),
            style = headline700(fontSize = 10.Ssp, color = Color.White),
        )
    }
}

@Composable
private fun ChannelAvatar(logoUrl: String?) {
    Box(
        modifier = Modifier
            .size(48.Sdp)
            .background(Color.White.copy(alpha = 0.12f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!logoUrl.isNullOrBlank()) {
            AsyncImage(
                model = logoUrl,
                contentDescription = null,
                modifier = Modifier.size(48.Sdp),
                contentScale = ContentScale.Crop,
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_nav_channel),
                contentDescription = null,
                tint = IptvTextPrimary,
                modifier = Modifier.size(24.Sdp)
            )
        }
    }
}

@Composable
private fun ActionButton(
    iconRes: Int,
    tint: Color,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.Sdp),
        modifier = Modifier.onClick(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.Sdp)
                .background(Color.White.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.Sdp)
            )
        }
        Text(
            text = label,
            style = headline400(fontSize = 10.Ssp, color = IptvTextSecondary),
        )
    }
}

@Composable
private fun RoundIconButton(iconRes: Int, tint: Color = IptvTextPrimary, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.Sdp)
            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            .onClick(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.Sdp)
        )
    }
}

@PhonePreview
@Composable
private fun WatchNowContentPreview() {
    AppViewTheme {
        WatchNowContent(
            channels = ChannelEntity.fakeList(),
            isMuted = false,
            pagePlayerMap = emptyMap(),
            statusBarHeight = 24.dp,
            onBack = {},
            onToggleMute = {},
            onToggleFavorite = {},
            onPageSelected = {}
        )
    }
}
