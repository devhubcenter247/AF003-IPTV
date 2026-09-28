package com.dongbv.alf_03_iptv.ads

import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.model.AdSplashConfig
import com.dongbv.alf_03_iptv.ads.wrapper.banner.BannerPlacement
import com.lib.ads.gma.ads.helper.adnative.NativeAdSpec
import com.lib.ads.gma.ads.helper.adnative.NativeAds
import com.lib.ads.gma.ads.helper.adnative.PreloadBufferState
import com.lib.ads.gma.ads.helper.banner.BannerAdSpec
import com.lib.ads.gma.ads.helper.banner.BannerAds
import com.lib.ads.gma.ads.helper.banner.params.BannerSize
import com.lib.ads.gma.ads.manager.InterstitialAdManager
import com.lib.ads.gma.ads.model.wrapper.ApAdError
import com.lib.ads.gma.ads.model.wrapper.InterstitialAdListener
import kotlinx.coroutines.flow.StateFlow

object AdPreloadManager {

    fun preloadNativeAdConfig(nativePlacement: NativePlacement, buffer: Int = 1) {
        if (!nativePlacement.canShowAds) return
        NativeAds.safePreload(
            NativeAdSpec(
                tag = nativePlacement.tag,
                adUnitIds = nativePlacement.listId,
                bufferSize = buffer,
            )
        )
    }
    fun preloadBannerConfig(bannerPlacement: BannerPlacement, size: BannerSize = BannerSize.LargePortraitAdaptive, buffer: Int = 1) {
        if (!bannerPlacement.canShowAds) return
        BannerAds.safePreload(
            BannerAdSpec(
                tag = bannerPlacement.tag,
                adUnitIds = bannerPlacement.listId,
                bufferSize = buffer,
            )
        )
    }

    fun getNativePreload(nativePlacement: NativePlacement): StateFlow<PreloadBufferState> {
        return NativeAds.stateFlow(nativePlacement.tag)
    }

    fun getAdCountPreload(nativePlacement: NativePlacement) =
        NativeAds.available(nativePlacement.tag)

    // inter splash

    fun preloadInterSplashConfig(
        adSplashConfig: AdSplashConfig, onInterReady: () -> Unit = {},
        onNextAction: () -> Unit = {}
    ) {
        val adUnitConfig = adSplashConfig.listAds
            .filter { it.enableAd && it.adUnit.isNotBlank() }.map { it.adUnit }
        if (!adSplashConfig.enable || adUnitConfig.isEmpty()) {
            onNextAction()
            return
        }
        var resolved = false
        fun continueOnce() {
            if (resolved) return
            resolved = true
            onNextAction()
        }
        InterstitialAdManager.loadSplashListAds(
            listId = adUnitConfig,
            timeOut = adSplashConfig.totalTimeout.coerceAtLeast(1),
            timeDelay = 0,
            listener = object : InterstitialAdListener {
                override fun onReady() {
                    if (resolved) return
                    resolved = true
                    onInterReady()
                }

                override fun onNextAction() = continueOnce()
                override fun onFailed(error: ApAdError) = continueOnce()
                override fun onFailedToShow(error: ApAdError) = continueOnce()
            }
        )
    }
}
