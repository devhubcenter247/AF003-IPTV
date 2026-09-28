package com.dongbv.alf_03_iptv.ads.wrapper.native.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.dongbv.alf_03_iptv.ads.model.LayoutNativeType
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdCard
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.lib.ads.gma.ads.model.wrapper.ApNativeAd

internal fun ApNativeAd?.isFacebookMediation(): Boolean {
    return this
        ?.nativeAd
        ?.getResponseInfo()
        ?.adapterClassName
        ?.substringAfterLast(".") == Mediation.Facebook.mediaName
}

sealed class Mediation(val mediaName: String) {
    data object Admob : Mediation("AdMobAdapter")
    data object Facebook : Mediation("FacebookMediationAdapter")
}

/**
 * Vẽ shimmer tương ứng với layout của [placement]. Dùng chung cho mọi cách
 * hiển thị native (NativeAdCard, wrapper, …).
 */
@Composable
fun NativeShimmerByLayout(
    placement: NativePlacement,
    isShow: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    when (placement.nativeConfig.layout) {
        LayoutNativeType.NativeMediumCtaBottom.type -> ShimmerMediumCtaBottomView(isShow, modifier)
        LayoutNativeType.NativeMediumCtaTop.type -> ShimmerMediumCtaTopView(isShow, modifier)
        LayoutNativeType.NativeMediumCtaBotRight.type -> ShimmerMediumCtaBotRightView(
            isShow,
            modifier
        )
        LayoutNativeType.MediumCtaRightBottom.type -> ShimmerMediumCtaBotRightView(isShow, modifier)
        LayoutNativeType.NativeMediumCtaTopRight.type -> ShimmerMediumCtaTopRightView(
            isShow,
            modifier
        )
        LayoutNativeType.NativeMediumMediaLeftCtaRight120.type -> ShimmerMediaLeftCtaRightView120(isShow,modifier)
        LayoutNativeType.MediumCtaRightTop.type -> ShimmerMediumCtaTopRightView(isShow, modifier)
        LayoutNativeType.NativeMediumMediaLeftCtaRight.type -> ShimmerMediaLeftCtaRightView(
            isShow,
            modifier
        )
        LayoutNativeType.NativeMediumMediaLeftCtaBottom.type -> ShimmerMediaLeftComposeView(
            isShow,
            modifier
        )
        LayoutNativeType.NativeMediumMediaLeftCtaTop.type -> ShimmerMediaLeftCtaTopView(
            isShow,
            modifier
        )
        LayoutNativeType.NativeSmallCtaBottom.type -> ShimmerSmallCtaBotView(isShow, modifier)
        LayoutNativeType.NativeSmallCtaTop.type -> ShimmerSmallCtaTopView(isShow, modifier)
        LayoutNativeType.NativeSmallCtaRight.type -> ShimmerSmallCtaRightView(isShow, modifier)
        else -> ShimmerMediaLeftComposeView(isShow, modifier)
    }
}

/**
 * Vẽ native ad đã load tương ứng với layout của [placement].
 */
@SuppressLint("ModifierParameter")
@Composable
private fun NativeAdByLayout(
    placement: NativePlacement,
    nativeAd: ApNativeAd,
    isMeta: Boolean = nativeAd.isFacebookMediation(),
    metaCtrLow: Boolean = false,
    nativeModifier: Modifier = Modifier,
    modifier: Modifier = Modifier.wrapContentHeight(),
) {
    val contentModifier = Modifier
        .then(modifier)
    Box(modifier = nativeModifier, contentAlignment = Alignment.Center) {
        when (placement.nativeConfig.layout) {
            LayoutNativeType.NativeMediumMediaLeftCtaBottom.type -> NativeMediaLeftCtaBot(
                nativeAdView = nativeAd,
                isMeta = isMeta,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeMediumMediaLeftCtaTop.type -> NativeMediaLeftCtaTop(
                nativeAdView = nativeAd,
                isMeta = isMeta,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeMediumCtaBottom.type -> NativeMediumCtaBottom(
                nativeAdView = nativeAd,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeMediumCtaTop.type -> NativeMediumCtaTop(
                nativeAdView = nativeAd,
                isMeta = isMeta,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeMediumMediaLeftCtaRight120.type -> {
                NativeMediumMediaLeftCtaRight120(
                    nativeAdView = nativeAd,
                    isMeta = isMeta,
                    isMetaLow = metaCtrLow,
                    modifier = contentModifier,
                )
            }
            LayoutNativeType.NativeMediumCtaBotRight.type -> NativeMediumCtaBotRight(
                nativeAdView = nativeAd,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.MediumCtaRightBottom.type -> NativeMediumCtaBotRight(
                nativeAdView = nativeAd,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeMediumCtaTopRight.type -> NativeMediumCtaTopRight(
                nativeAdView = nativeAd,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.MediumCtaRightTop.type -> NativeMediumCtaTopRight(
                nativeAdView = nativeAd,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeMediumMediaLeftCtaRight.type -> NativeMediumMediaLeftCtaRight(
                nativeAdView = nativeAd,
                isMeta = isMeta,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeSmallCtaBottom.type -> NativeSmallCtaBot(
                nativeAdView = nativeAd,
                isMeta = isMeta,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeSmallCtaTop.type -> NativeSmallCtaTop(
                nativeAdView = nativeAd,
                isMeta = isMeta,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )

            LayoutNativeType.NativeSmallCtaRight.type -> NativeSmallCtaRight(
                nativeAdView = nativeAd,
                isMetaLow = metaCtrLow,
                modifier = contentModifier,
            )
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun NativeComposeView(
    nativePreloadConfig: Pair<NativeAdHolderConfig?, NativePlacement>? = null,
    metaCtrLow: Boolean = false,
    nativeModifier: Modifier = Modifier,
    modifier: Modifier = Modifier.wrapContentHeight(),
) {
    val isPreview = LocalInspectionMode.current
    if (nativePreloadConfig != null) {
        if(!nativePreloadConfig.second.canShowAds) return
        val placement = nativePreloadConfig.second
        NativeAdCard(
            holder = nativePreloadConfig.first,
            modifier = modifier.background(NativeAdColors.colorBgNativeAds),
            loading = { isShow ->
                NativeShimmerByLayout(placement, isShow, Modifier.fillMaxWidth())
            },
            nativeView = { nativeAd ->
                NativeAdByLayout(
                    placement = placement,
                    nativeAd = nativeAd,
                    isMeta = nativeAd.isFacebookMediation(),
                    metaCtrLow = metaCtrLow,
                    nativeModifier = nativeModifier,
                )
            }
        )
    } else if (isPreview) {
        ShimmerMediaLeftComposeView(
            isShowShimmer = true,
            modifier = nativeModifier
        )
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun NativeComposeViewOnboarding(
    nativePreloadConfig: Pair<NativeAdHolderConfig?, NativePlacement>? = null,
    metaCtrLow: Boolean = false,
    nativeModifier: Modifier = Modifier,
    modifier: Modifier = Modifier.wrapContentHeight(),
) {
    val isPreview = LocalInspectionMode.current
    if (nativePreloadConfig != null) {
        val placement = nativePreloadConfig.second
        NativeAdCard(
            holder = nativePreloadConfig.first,
            modifier = nativeModifier,
            loading = { isShow ->
                NativeShimmerByLayout(placement, isShow, Modifier.fillMaxWidth())
            },
            nativeView = { nativeAd ->
                NativeAdByLayout(
                    placement = placement,
                    nativeAd = nativeAd,
                    isMeta = nativeAd.isFacebookMediation(),
                    metaCtrLow = metaCtrLow,
                    nativeModifier = nativeModifier,
                    modifier = modifier,
                )
            }
        )
    } else if (isPreview) {
        ShimmerMediaLeftComposeView(
            isShowShimmer = true,
            modifier = nativeModifier
        )
    }
}
@SuppressLint("ModifierParameter")
@Composable
fun NativeComposeFullView(
    nativePreloadConfig: NativeAdHolderConfig? = null,
    metaCtrLow: Boolean = false,
    nativeModifier: Modifier = Modifier,
    modifier: Modifier = Modifier.wrapContentHeight(),
) {
    val isPreview = LocalInspectionMode.current
    if (nativePreloadConfig != null) {

        NativeAdCard(
            holder = nativePreloadConfig,
            modifier = Modifier.background(NativeAdColors.backgroundFull).then(nativeModifier),
            loading = { isShow ->
                ShimmerNativeFullScreenAd(isShowShimmer = isShow, modifier)
            },
            nativeView = { nativeAd ->
                val isMeta = nativeAd.isFacebookMediation()
                NativeFullScreenAd(nativeAd, isMetaLow = metaCtrLow && isMeta, modifier)
            })
    } else if (isPreview) {
        ShimmerNativeFullScreenAd(isShowShimmer = true, nativeModifier)
    }
}


@Preview(showBackground = true)
@Composable
fun NativeFullComposeViewPreview() {
    AppViewTheme {
        NativeComposeFullView()
    }
}

@Preview(showBackground = true)
@Composable
fun NativeComposeViewPreview() {
    AppViewTheme {
        NativeComposeView()
    }
}
