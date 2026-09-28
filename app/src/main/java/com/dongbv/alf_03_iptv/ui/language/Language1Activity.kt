package com.dongbv.alf_03_iptv.ui.language

import com.dongbv.alf_03_iptv.analytics.AppScreen
import com.dongbv.alf_03_iptv.ads.AdPreloadManager
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.app.remote.remoteConfig

class Language1Activity : LanguageActivity() {
    override val analyticsScreen = AppScreen.LANGUAGE_1

    override val screenType: LanguageScreenType = LanguageScreenType.Language1
    override fun nativePlacement(): NativePlacement = NativePlacement.Language1

    override fun initialize() {
        nativeAdHolder.request()
        AdPreloadManager.preloadNativeAdConfig(NativePlacement.Onboarding, remoteConfig.onboardingScreen.enableCount())
    }
}
