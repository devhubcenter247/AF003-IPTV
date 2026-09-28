package com.dongbv.alf_03_iptv.ads.model

import com.dongbv.alf_03_iptv.BuildConfig
import com.google.gson.annotations.SerializedName

data class AdInterConfig(
    @SerializedName("time_interval_ms")
    val timeInterval: Long,
    @SerializedName("time_steps")
    val listAds: List<AdUnitConfig>,
    @SerializedName("enable")
    var enable: Boolean,
) {
    companion object {
        fun createAdUnitConfig(
            enbHf: Boolean = false,
            hfAdId: String = "",
            adUnit: String = "",
            enableAd: Boolean = true
        ): List<AdUnitConfig> = if (BuildConfig.build_debug) listOf(
            AdUnitConfig(
                enableAd = true,
                adUnit = "/21775744923/example/interstitial"
            )
        ) else {
            if (hfAdId.isNotBlank()) {
                listOf(
                    AdUnitConfig(enableAd = enbHf, adUnit = hfAdId),
                    AdUnitConfig(enableAd = enableAd, adUnit = adUnit)
                )
            } else {
                listOf(
                    AdUnitConfig(enableAd = enableAd, adUnit = adUnit)
                )
            }
        }

        val adInterConfig = AdInterConfig(
            enable = true,
            timeInterval = 120000,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/7142913794",
                adUnit = "ca-app-pub-1713129912546517/4980794044",
                enableAd = true
            )
        )
    }
}
