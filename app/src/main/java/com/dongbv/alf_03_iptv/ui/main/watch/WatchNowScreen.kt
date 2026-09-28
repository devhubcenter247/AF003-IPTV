package com.dongbv.alf_03_iptv.ui.main.watch

import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeFullView
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.GradientCircularProgressIndicator
import com.dongbv.alf_03_iptv.style.PhonePreview
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.borderStart
import com.dongbv.alf_03_iptv.style.onClick
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.ChannelListItem
import com.dongbv.alf_03_iptv.ui.component.borderState
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.ui.main.play.LiveBadge
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.IptvBgPrimary
import com.dongbv.alf_03_iptv.widget.IptvSuccess
import com.dongbv.alf_03_iptv.widget.IptvTextPrimary
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.headline400
import com.dongbv.alf_03_iptv.widget.headline600
import com.dongbv.alf_03_iptv.widget.headline700
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdDisplayState
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Stable
interface WatchNowActions {
    fun onBack()
    fun onToggleMute()
    fun onToggleFavorite(channelId: Long)
    fun onPageSelected(page: Int)

    companion object {
        val None = object : WatchNowActions {
            override fun onBack() = Unit
            override fun onToggleMute() = Unit
            override fun onToggleFavorite(channelId: Long) = Unit
            override fun onPageSelected(page: Int) = Unit
        }
    }
}

@Composable
fun WatchNowContent(
    channels: List<ChannelEntity>,
    isMuted: Boolean,
    isShowBack: Boolean = true,
    modifier: Modifier = Modifier,
    pagePlayerMap: Map<Int, ExoPlayer>,
    autoNextEvent: SharedFlow<Int>? = null,
    actions: WatchNowActions = WatchNowActions.None,
    nativeFullAdHolder: NativeAdHolderConfig? = null,
) {
    if (channels.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            GradientCircularProgressIndicator()
        }
        return
    }

    val showNativeAds = nativeFullAdHolder != null &&
        NativePlacement.OnboardingFull.canShowAds &&
        NativePlacement.OnboardingFull.listId.any { it.isNotBlank() } &&
        !AppPurchase.getInstance().isPurchased()
    val displayPages = remember(channels, showNativeAds) {
        if (!showNativeAds) {
            channels.indices.map { it as Int? }
        } else {
            buildList {
                channels.indices.forEachIndexed { index, channelIndex ->
                    add(channelIndex)
                    if ((index + 1) % 5 == 0 && index < channels.lastIndex) add(null)
                }
            }
        }
    }
    val pagerState = rememberPagerState { displayPages.size }

    LaunchedEffect(autoNextEvent, displayPages) {
        autoNextEvent?.collect { nextPage ->
            val targetPage = displayPages.indexOf(nextPage)
            if (targetPage >= 0) {
                pagerState.animateScrollToPage(targetPage)
            }
        }
    }

    LaunchedEffect(pagerState, displayPages, actions) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page -> displayPages.getOrNull(page)?.let(actions::onPageSelected) }
    }

    LaunchedEffect(nativeFullAdHolder, pagerState, displayPages) {
        val holder = nativeFullAdHolder ?: return@LaunchedEffect
        combine(
            holder.state,
            snapshotFlow { pagerState.settledPage },
        ) { adState, page -> adState to page }
            .collect { (adState, page) ->
                val adUnavailable = adState is NativeAdDisplayState.Error ||
                    adState is NativeAdDisplayState.Cancelled
                if (adUnavailable && page in displayPages.indices && displayPages[page] == null) {
                    val nextChannelPage = ((page + 1)..displayPages.lastIndex)
                        .firstOrNull { displayPages[it] != null }
                    if (nextChannelPage != null) pagerState.scrollToPage(nextChannelPage)
                }
            }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val isTablet = maxWidth >= 600.dp

        if (isTablet) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                ) {
                    VerticalPager(
                        state = pagerState,
                        beyondViewportPageCount = 2,
                        modifier = Modifier.fillMaxSize(),
                    ) { page ->
                        val channelIndex = displayPages[page]
                        if (channelIndex == null) {
                            NativeComposeFullView(
                                nativePreloadConfig = nativeFullAdHolder,
                                nativeModifier = Modifier.fillMaxSize(),
                            )
                        } else {
                            ChannelReelPage(
                                channel = channels[channelIndex],
                                isShowBack = isShowBack,
                                player = pagePlayerMap[channelIndex],
                                isMuted = isMuted,
                                actions = actions,
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxHeight()
                    
                ) {
                    Text(
                        text = stringResource(R.string.channels),
                        style = headline700(fontSize = 16.Ssp, color = IptvTextPrimary),
                        modifier = Modifier.padding(16.Sdp)
                    )

                    val scope = androidx.compose.runtime.rememberCoroutineScope()
                    LazyColumn(
                        state = rememberLazyListState(),
                        verticalArrangement = Arrangement.spacedBy(8.Sdp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(channels, key = { it.id }) { channel ->
                            val selectedChannelIndex = displayPages.getOrNull(pagerState.settledPage)
                            val isSelected = selectedChannelIndex != null && channels[selectedChannelIndex].id == channel.id
                            ChannelListItem(
                                channel = channel,
                                isTablet = true,
                                isSelected = isSelected,
                                onClick = {
                                    val index = channels.indexOf(channel)
                                    if (index >= 0) {
                                        scope.launch {
                                            val targetPage = displayPages.indexOf(index)
                                            if (targetPage >= 0) pagerState.animateScrollToPage(targetPage)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        } else {
            VerticalPager(
                state = pagerState,
                beyondViewportPageCount = 2,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val channelIndex = displayPages[page]
                if (channelIndex == null) {
                    NativeComposeFullView(
                        nativePreloadConfig = nativeFullAdHolder,
                        nativeModifier = Modifier.fillMaxSize(),
                    )
                } else {
                    ChannelReelPage(
                        channel = channels[channelIndex],
                        isShowBack = isShowBack,
                        player = pagePlayerMap[channelIndex],
                        isMuted = isMuted,
                        actions = actions,
                    )
                }
            }
        }
    }
}

@Composable
private fun ChannelReelPage(
    channel: ChannelEntity,
    isShowBack: Boolean = true,
    player: ExoPlayer?,
    isMuted: Boolean,
    actions: WatchNowActions,
) {
    var showControls by remember { mutableStateOf(value = true) }
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
            delay(3_000.milliseconds)
            showControls = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onClick { showControls = !showControls }
    ) {
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
            if (!channel.logo.isNullOrBlank()) {
                AsyncImage(
                    model = channel.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_nav_channel),
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
            )
        }

        if (isBuffering) {
            GradientCircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

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
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarPadding()
                        .padding(horizontal = 16.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(isShowBack) {
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
                    }
                    Spacer(Modifier.weight(1f))
                    LiveBadge()
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.Sdp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.Sdp)
                ) {
                    ActionButton(
                        iconRes = if (isFavorite) R.drawable.ic_app_favorite_minus
                        else R.drawable.ic_app_favorite_add,
                        tint = if (isFavorite) Color.Red else white,
                        label = stringResource(R.string.favorite),
                        onClick = {
                            isFavorite = !isFavorite
                            actions.onToggleFavorite(channel.id)
                        }
                    )
                    ActionButton(
                        iconRes = if (isMuted) R.drawable.ic_app_volume_off else R.drawable.ic_app_volume_on,
                        tint = IptvTextPrimary,
                        label = stringResource(R.string.sound),
                        onClick = actions::onToggleMute
                    )
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .paddingHorizontal(16.Sdp)
                        .align(Alignment.BottomStart)
                ) {
                    ChannelAvatar(logoUrl = channel.logo)
                    Column(
                        modifier = Modifier
                            .padding(start = 12.Sdp, bottom = 32.Sdp)
                    ) {
                        if (!channel.groupName.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .background(IptvAccent.copy(alpha = 0.15f), CircleShape)
                                    .padding(horizontal = 4.Sdp, vertical = 2.Sdp)
                            ) {
                                Text(
                                    text = channel.groupName,
                                    style = headline400(fontSize = 9.Ssp, color = IptvAccent),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(Modifier.height(4.Sdp))
                        }
                        Text(
                            text = channel.name,
                            style = headline700(fontSize = 14.Ssp, color = IptvTextPrimary),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun ChannelAvatar(logoUrl: String?) {
    Box(
        modifier = Modifier
            .size(48.Sdp)

            .background(Color.White.copy(alpha = 0.12f), CircleShape)
            .borderState(true)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        var isLogoLoadError by remember(logoUrl) { mutableStateOf(false) }
        if (!logoUrl.isNullOrBlank() && !isLogoLoadError) {
            AsyncImage(
                model = logoUrl,
                contentDescription = null,
                contentScale = ContentScale.None,
                onState = { state -> isLogoLoadError = state is AsyncImagePainter.State.Error },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.Sdp)

            )
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_channel),
                    contentDescription = null,
                    tint = colorSecondary,
                    modifier = Modifier.size(30.Sdp)
                )
            }
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
        verticalArrangement = Arrangement.spacedBy(3.Sdp),
        modifier = Modifier.onClick(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(32.Sdp)
                .background(Color.White.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = tint,
                modifier = Modifier.padding(8.Sdp)
            )
        }
        Text(
            text = label,
            style = headline400(fontSize = 10.Ssp, color = colorSecondary),
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
            modifier = Modifier.backgroundResource(R.drawable.img_bg_app),
            pagePlayerMap = emptyMap(),
            actions = WatchNowActions.None
        )
    }
}
