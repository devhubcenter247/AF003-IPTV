package com.dongbv.alf_03_iptv.ui.main.play

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dongbv.alf_03_iptv.ads.wrapper.banner.BannerPlacement
import com.dongbv.alf_03_iptv.ads.wrapper.banner.rememberBannerPlacement
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeView
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdPreloadHolderOptions
import com.lib.ads.gma.ads.helper.adnative.preload.rememberNativeAdPreload
import com.lib.ads.gma.ads.helper.banner.params.BannerSize
import com.lib.ads.gma.ads.helper.banner.preload.BannerAdCard

private const val CHANNELS_PER_NATIVE_AD = 5

/** Separate lazy rows keep channel identity and only compose nearby native ad holders. */
internal fun LazyListScope.playChannelItems(
    channels: List<ChannelEntity>,
    showNativeAds: Boolean,
    channelContent: @Composable (ChannelEntity) -> Unit,
) {
    val nativeCount = if (showNativeAds) channels.size / CHANNELS_PER_NATIVE_AD else 0
    fun isNativeAd(index: Int) = showNativeAds && (index + 1) % (CHANNELS_PER_NATIVE_AD + 1) == 0
    fun channelIndex(index: Int) = if (showNativeAds) index - index / (CHANNELS_PER_NATIVE_AD + 1) else index

    items(
        count = channels.size + nativeCount,
        key = { index ->
            if (isNativeAd(index)) "native_after_${channels[channelIndex(index) - 1].id}"
            else "channel_${channels[channelIndex(index)].id}"
        },
        contentType = { index -> if (isNativeAd(index)) "native" else "channel" },
    ) { index ->
        if (isNativeAd(index)) {
            PlayChannelNativeAd(channels[channelIndex(index) - 1].id)
        } else {
            channelContent(channels[channelIndex(index)])
        }
    }
}

@Composable
internal fun PlayChannelFooterBanner() {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        // Fit the fixed footer to the available screen width.
        val holder = rememberBannerPlacement(
            bannerPlacement = BannerPlacement.BannerAll,
            autoReloadOnResume = false,
            bannerSize = BannerSize.Width(maxWidth.value.toInt().coerceAtLeast(1)),
            isCollapsible = false,
        )
        if (holder != null) {
            BannerAdCard(holder = holder, modifier = Modifier.fillMaxWidth())
        }
    }
}


@Composable
private fun PlayChannelNativeAd(afterChannelId: Long) {
    val placement = NativePlacement.NativePlayList
    val holder = rememberNativeAdPreload(
        tag = "${placement.tag}_$afterChannelId",
        options = NativeAdPreloadHolderOptions(
            fallbackAdUnitIds = placement.listId,
            autoRequestOnStart = true,
            autoReloadOnResume = false,
            canShowAds = placement.canShowAds,
            destroyOnDispose = true,
        ),
    )
    NativeComposeView(
        nativePreloadConfig = holder to placement,
        modifier = Modifier.fillMaxWidth(),
    )
}
