package com.dongbv.alf_03_iptv.ads.model

import com.dongbv.alf_03_iptv.BuildConfig
import com.google.gson.annotations.SerializedName

data class AdBannerConfig(
    @SerializedName("is_collapsible")
    val isCollapsible: Boolean,
    @SerializedName("list_ads")
    val listAds: List<AdUnitConfig>,
    @SerializedName("time_reload_banner")
    val timeReloadBanner: Long = 0,
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
                adUnit = listOf(
                    "/21775744923/example/adaptive-banner",
                    "/21775744923/example/fixed-size-banner"
                ).random()
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

        val adBannerAllConfig = AdBannerConfig(
            enable = true,
            isCollapsible = true,
            timeReloadBanner = 15000,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/8416609235",
                enableAd = true,
                adUnit = "ca-app-pub-1713129912546517/6473238102",
            )
        )
        val adBannerSplashConfig = AdBannerConfig(
            enable = true,
            isCollapsible = true,
            timeReloadBanner = 15000,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/5982017580",
                enableAd = true,
                adUnit = "ca-app-pub-1713129912546517/7678242630",

            )
        )
    }
}
