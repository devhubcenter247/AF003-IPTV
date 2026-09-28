package com.dongbv.alf_03_iptv.ads.wrapper.banner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.lib.ads.gma.ads.billing.AppPurchase
import androidx.compose.ui.platform.LocalInspectionMode
import com.dongbv.alf_03_iptv.ads.model.AdBannerConfig

import com.lib.ads.gma.ads.helper.banner.params.BannerAdConfig
import com.lib.ads.gma.ads.helper.banner.params.BannerSize
import com.lib.ads.gma.ads.helper.banner.preload.BannerAdHolder
import com.lib.ads.gma.ads.helper.banner.preload.rememberBannerAd
import com.lib.ads.gma.ads.helper.utils.BannerCollapseGravity
import com.sdp.ssp.android.Sdp


sealed class BannerPlacement {
    val tag: String = this::class.java.simpleName
    abstract val adConfig: AdBannerConfig
    open val listId: List<String> = adConfig.listAds.filter { it.enableAd }.map { it.adUnit }
    open val canShowAds: Boolean = adConfig.enable
    open val isCollapsible: Boolean = adConfig.isCollapsible
    open val timeReloadBanner: Long =  adConfig.timeReloadBanner

    data object BannerAll : BannerPlacement() {
        override val adConfig: AdBannerConfig
            get() = remoteConfig.bannerAllConfig
    }

    data object BannerSplash : BannerPlacement() {
        override val adConfig: AdBannerConfig
            get() = remoteConfig.bannerSplashConfig
    }
}

/** A screen-scoped banner; null when ads are disabled, purchased, or in Preview. */
@Composable
fun rememberBannerPlacement(
    bannerPlacement: BannerPlacement,
    autoReloadOnResume: Boolean = true,
    bannerSize: BannerSize = BannerSize.LargePortraitAdaptive,
    isCollapsible: Boolean = bannerPlacement.isCollapsible,
): BannerAdHolder? {
    if (LocalInspectionMode.current) return null

    val enabled = bannerPlacement.canShowAds && !AppPurchase.getInstance().isPurchased()
    val adUnitIds = bannerPlacement.listId
    if (!enabled || adUnitIds.isEmpty()) return null

    val config = remember(bannerPlacement, adUnitIds, isCollapsible, autoReloadOnResume, bannerSize) {
        BannerAdConfig(
            listId = adUnitIds,
            canShowAds = true,
            canReloadAds = autoReloadOnResume,
        ).apply {
            collapsibleGravity = if (isCollapsible) BannerCollapseGravity.BOTTOM.value else null
            size = bannerSize
        }
    }
    return rememberBannerAd(config = config, autoReloadOnResume = autoReloadOnResume)
}
