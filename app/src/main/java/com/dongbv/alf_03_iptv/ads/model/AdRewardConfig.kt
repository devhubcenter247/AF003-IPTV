package com.dongbv.alf_03_iptv.ads.model

import com.dongbv.alf_03_iptv.BuildConfig
import com.google.gson.annotations.SerializedName

data class AdRewardConfig(
    @SerializedName("list_ads")
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
                adUnit = listOf(
                    "/21775744923/example/rewarded",
                    "/21775744923/example/rewarded-interstitial"
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

        val rewardedEdit = AdRewardConfig(
            enable = true,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/4733989876",
                adUnit = "ca-app-pub-1713129912546517/1289568880",
                enableAd = true
            )
        )
    }
}
