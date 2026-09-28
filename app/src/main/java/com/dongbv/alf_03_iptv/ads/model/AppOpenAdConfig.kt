package com.dongbv.alf_03_iptv.ads.model

import com.dongbv.alf_03_iptv.BuildConfig
import com.google.gson.annotations.SerializedName

data class AppOpenAdConfig(
    @SerializedName("list_ads")
    val listAds: List<AdUnitConfig>,
    @SerializedName("enable")
    var enable: Boolean,
) {
    companion object {
        fun createAdUnitConfig(
            enbHf: Boolean = false,
            hfAdId: String = "",
            enableAd: Boolean = true,
            adUnit: String = "",
        ): List<AdUnitConfig> = if (BuildConfig.build_debug) listOf(
            AdUnitConfig(
                enableAd = true,
                adUnit = "/21775744923/example/app-open"
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

        val appOpenAdConfig = AppOpenAdConfig(
            enable = true,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/8315822873",
                enableAd = true,
                adUnit = "ca-app-pub-1713129912546517/2293340001",
            )
        )
    }
}
