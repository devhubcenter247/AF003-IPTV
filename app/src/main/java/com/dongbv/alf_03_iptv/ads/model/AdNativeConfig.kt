package com.dongbv.alf_03_iptv.ads.model

import com.dongbv.alf_03_iptv.BuildConfig
import com.google.gson.annotations.SerializedName


data class AdNativeConfig(
    @SerializedName("type_layout")
    var layout: String,
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
                adUnit = listOf(
                    "/21775744923/example/native",
                    "/21775744923/example/native-video"
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

        val nativeSplash = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeMediumMediaLeftCtaBottom.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/4378766656",
                adUnit = "ca-app-pub-1713129912546517/7780627487",
                enableAd = true
            )
        )

        val nativeLanguage = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeMediumMediaLeftCtaBottom.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/2533993099",
                adUnit = "ca-app-pub-1713129912546517/9346547523",
                enableAd = true
            )
        )

        val nativeLanguageDup = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeMediumMediaLeftCtaBottom.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/6178556180",
                adUnit = "ca-app-pub-1713129912546517/8889796652",
                enableAd = true
            )
        )

        val nativeOb = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeMediumMediaLeftCtaBottom.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/3249517818",
                adUnit = "ca-app-pub-1713129912546517/3985695623",
                enableAd = true
            )
        )

        val nativeObFscr = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.Other.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/1468057501",
                adUnit = "ca-app-pub-1713129912546517/5961122271",
                enableAd = true
            )
        )

        val nativeFeature = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.Other.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/1481140574",
                adUnit = "ca-app-pub-1713129912546517/1533111466",
                enableAd = true
            )
        )

        val nativeSmallAll = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeSmallCtaBottom.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/8613915744",
                adUnit = "ca-app-pub-1713129912546517/7594748084",
                enableAd = true
            )
        )

        val nativeSmallCtaRight = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeSmallCtaRight.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/2163289552",
                adUnit = "ca-app-pub-1713129912546517/9662882928",
                enableAd = true
            )
        )

        val nativeMediumAll = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeMediumMediaLeftCtaRight120.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/2222999302",
                adUnit = "ca-app-pub-1713129912546517/1359532281",
                enableAd = true
            )
        )

        val nativeMediaLeft = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.NativeMediumMediaLeftCtaRight.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/2589567485",
                adUnit = "ca-app-pub-1713129912546517/4376577866",
                enableAd = true
            )
        )
        val nativePlayList = AdNativeConfig(
            enable = true,
            layout = LayoutNativeType.Other.type,
            listAds = createAdUnitConfig(
                enbHf = false,
                hfAdId = "ca-app-pub-1713129912546517/2589567485",
                adUnit = "ca-app-pub-1713129912546517/4376577866",
                enableAd = true
            )
        )
    }


    fun isNativeSmall() = this.layout.contains("native_small")
    fun isNativeLeftMedia() = this.layout.contains("media_left")
    override fun toString(): String {
        val adsSummary = listAds
            .mapIndexed { index, ad ->
                "[$index] ${ad}"
            }
            .joinToString(separator = ", ")

        return "AdNativeConfig(" +
                "enable=$enable, " +
                "layout='$layout', " +
                "nativeSmall=${isNativeSmall()}, " +
                "leftMedia=${isNativeLeftMedia()}, " +
                "listAdsSize=${listAds.size}, " +
                "listAds=[$adsSummary]" +
                ")"
    }
}

sealed class LayoutNativeType(val type: String) {
    data object NativeSmallCtaBottom : LayoutNativeType("native_small_cta_bottom")
    data object NativeSmallCtaTop : LayoutNativeType("native_small_cta_top")
    data object NativeSmallCtaRight : LayoutNativeType("native_small_cta_right")
    data object NativeMediumCtaBottom : LayoutNativeType("native_medium_cta_bottom")
    data object NativeMediumCtaTop : LayoutNativeType("native_medium_cta_top")
    data object MediumCtaRightBottom : LayoutNativeType("medium_cta_right_bottom")
    data object MediumCtaRightTop : LayoutNativeType("medium_cta_right_top")
    data object NativeMediumMediaLeftCtaBottom :
        LayoutNativeType("native_medium_media_left_cta_bottom")

    data object NativeMediumMediaLeftCtaRight :
        LayoutNativeType("native_medium_media_left_cta_right")
    data object NativeMediumMediaLeftCtaRight120 :
        LayoutNativeType("native_medium_media_left_cta_right_120")

    data object NativeMediumMediaLeftCtaTop : LayoutNativeType("native_medium_media_left_cta_top")
    data object NativeMediumCtaBotRight : LayoutNativeType("native_medium_cta_bot_right")
    data object NativeMediumCtaTopRight : LayoutNativeType("native_medium_cta_top_right")
    data object Other : LayoutNativeType("other")
}
