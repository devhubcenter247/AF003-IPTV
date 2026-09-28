package com.dongbv.alf_03_iptv.ui.language

import com.dongbv.alf_03_iptv.analytics.AppScreen
import com.dongbv.alf_03_iptv.ads.AdPreloadManager
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement

class Language2Activity : LanguageActivity() {
    override val analyticsScreen = AppScreen.LANGUAGE_2

    override val screenType: LanguageScreenType = LanguageScreenType.Language2

    override fun nativePlacement(): NativePlacement = NativePlacement.Language2

    override fun initialize() {
        nativeAdHolder.request()
        AdPreloadManager.preloadNativeAdConfig(NativePlacement.OnboardingFull)
    }
}
