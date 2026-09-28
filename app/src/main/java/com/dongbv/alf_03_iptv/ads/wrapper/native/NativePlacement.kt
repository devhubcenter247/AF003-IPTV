package com.dongbv.alf_03_iptv.ads.wrapper.native

import com.dongbv.alf_03_iptv.ads.model.AdNativeConfig
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.lib.ads.gma.ads.helper.utils.AdOptionVisibility



sealed class NativePlacement {
    val tag: String = this::class.java.simpleName
    abstract val listId: List<String>
    abstract val canShowAds: Boolean
    open val adVisibility: AdOptionVisibility = AdOptionVisibility.GONE
    abstract val nativeConfig: AdNativeConfig


    data object Language1 : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeLanguageConfig.listAds.filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeLanguageConfig.enable
        override val nativeConfig get() = remoteConfig.nativeLanguageConfig
    }

    data object NativeSplash : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeSplashConfig.listAds.filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeSplashConfig.enable
        override val nativeConfig get() = remoteConfig.nativeSplashConfig
    }

    data object Language2 : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeLanguageDupConfig.listAds.filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeLanguageDupConfig.enable
        override val nativeConfig get() = remoteConfig.nativeLanguageDupConfig
    }

    data object Onboarding : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeObConfig.listAds.filter { it.enableAd }.map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeObConfig.enable
        override val adVisibility = AdOptionVisibility.INVISIBLE
        override val nativeConfig get() = remoteConfig.nativeObConfig
    }

    data object NativeFeature : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeFeature.listAds.filter { it.enableAd }.map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeFeature.enable
        override val nativeConfig get() = remoteConfig.nativeFeature
    }

    data object OnboardingFull : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeObFscrConfig.listAds.filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeObFscrConfig.enable
        override val nativeConfig get() = remoteConfig.nativeObFscrConfig
    }

    data object NativeMediumAll : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeMediumAllConfig.listAds.filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeMediumAllConfig.enable
        override val nativeConfig get() = remoteConfig.nativeMediumAllConfig
    }

    data object NativePlayList : NativePlacement() {
        override val listId
            get() = remoteConfig.nativePlayList.listAds
                .filter { it.enableAd && it.adUnit.isNotBlank() }.map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativePlayList.enable
        override val nativeConfig get() = remoteConfig.nativePlayList
    }

    data object NativeSmallAll : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeSmallAllConfig.listAds.filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeSmallAllConfig.enable
        override val nativeConfig get() = remoteConfig.nativeSmallAllConfig
    }

    data object NativeSmallCtaRight : NativePlacement() {
        override val listId
            get() = remoteConfig.nativeSmallCtaRightConfig.listAds.filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds get() = remoteConfig.nativeSmallCtaRightConfig.enable
        override val nativeConfig get() = remoteConfig.nativeSmallCtaRightConfig
    }

    override fun toString(): String {
        return "NativePlacement(listId=$listId, canShowAds=$canShowAds, adVisibility=$adVisibility, layout:${nativeConfig.layout}), Config: $nativeConfig"
    }

    companion object {

    }
}
