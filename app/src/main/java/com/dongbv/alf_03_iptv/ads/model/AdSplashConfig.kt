package com.dongbv.alf_03_iptv.ads.model

import com.dongbv.alf_03_iptv.BuildConfig
import com.google.gson.annotations.SerializedName

data class AdSplashConfig(
    @SerializedName("total_timeout_ms")
    val totalTimeout: Long,
    @SerializedName("list_ads")
    val listAds: List<SplashAdConfig>,
    @SerializedName("enable")
    var enable: Boolean,
) {
    companion object {
        fun createAdUnitConfig(
            enbHf: Boolean = false,
            hfAdId: String = "",
            hfTimeout: Long = 30000,
            enableAd: Boolean = true,
            adUnit: String = "",
            timeout: Long = 30000,

        ): List<SplashAdConfig> = if (BuildConfig.build_debug) listOf(
            SplashAdConfig(
                enableAd = true,
                timeout = 30000,
                adUnit = "/21775744923/example/interstitial"
            )
        ) else {
            if (hfAdId.isNotBlank()) {
                listOf(
                    SplashAdConfig(enableAd = enbHf, timeout = hfTimeout, adUnit = hfAdId),
                    SplashAdConfig(enableAd = enableAd, timeout = timeout, adUnit = adUnit)
                )
            } else {
                listOf(
                    SplashAdConfig(enableAd = enableAd, timeout = timeout, adUnit = adUnit)
                )
            }
        }

        val adSplashConfig = AdSplashConfig(
            enable = true,
            totalTimeout = 30000,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/8481663199",
                hfTimeout = 30000,
                enableAd = true,
                adUnit = "ca-app-pub-1713129912546517/7496823937",
                timeout = 30000,
            )
        )
    }
}
