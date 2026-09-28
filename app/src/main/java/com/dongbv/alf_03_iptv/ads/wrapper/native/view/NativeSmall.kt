package com.dongbv.alf_03_iptv.ads.wrapper.native.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.style.PhonePreview
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.lightGrey
import com.dongbv.alf_03_iptv.style.paddingStart
import com.dongbv.alf_03_iptv.style.rememberShimmerState
import com.dongbv.alf_03_iptv.style.shimmer
import com.dongbv.alf_03_iptv.style.white
import com.lib.ads.gma.ads.helper.adnative.api.NativeAdView
import com.lib.ads.gma.ads.model.wrapper.ApNativeAd
import com.sdp.ssp.android.Sdp


@Composable
fun NativeSmallCtaBot(
    nativeAdView: ApNativeAd,
    isMeta: Boolean = false,
    isMetaLow: Boolean = false,
    modifier: Modifier = Modifier
) {
    NativeAdView(nativeAd = nativeAdView, modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (isMeta && isMetaLow) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                        .padding(top = 12.Sdp)
                ) {
                    NativeIconContent()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.Sdp)
                    ) {
                        NativeHeadlineRow()
                        MetaTextSponsor()
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .paddingStart(12.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NativeBodyText(modifier = Modifier.weight(1f))
                    NativeCtaButton(
                        modifier = Modifier
                            .fillMaxWidth(0.38f)
                            .padding(horizontal = 10.Sdp)
                            .padding(top = 10.Sdp)
                            .padding(bottom = 12.Sdp),
                        isMetaLow = true
                    )
                }

            } else if (isMeta && !isMetaLow) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                        .padding(top = 12.Sdp)
                ) {
                    NativeIconContent()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.Sdp)
                    ) {
                        NativeHeadlineRow()
                        MetaTextSponsor()
                        NativeBodyText()
                    }
                }
                NativeCtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                        .padding(top = 10.Sdp)
                        .padding(bottom = 12.Sdp)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                        .padding(top = 12.Sdp)
                ) {
                    NativeIconContent()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.Sdp)
                    ) {
                        NativeHeadlineRow()
                        NativeBodyText()
                    }
                }
                NativeCtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                        .padding(top = 10.Sdp)
                        .padding(bottom = 12.Sdp)
                )
            }
        }
    }
}

@Composable
fun NativeSmallCtaTop(
    nativeAdView: ApNativeAd,
    isMeta: Boolean = false,
    isMetaLow: Boolean = false,
    modifier: Modifier = Modifier
) {
    NativeAdView(nativeAd = nativeAdView, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.Sdp)
        ) {
            if (isMeta && isMetaLow) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.Sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NativeBodyText(
                        Modifier
                            .weight(1f)
                            .paddingStart(12.Sdp)
                    )
                    NativeCtaButton(
                        modifier = Modifier
                            .fillMaxWidth(0.38f)
                            .padding(horizontal = 12.Sdp),
                        isMetaLow = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                ) {
                    NativeIconContent()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.Sdp)
                    ) {
                        NativeHeadlineRow()
                        MetaTextSponsor()
                    }
                }
            } else if (isMeta && !isMetaLow) {
                NativeCtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                        .padding(vertical = 12.Sdp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                ) {
                    NativeIconContent()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.Sdp)
                    ) {
                        NativeHeadlineRow()
                        MetaTextSponsor()
                        NativeBodyText()
                    }
                }
            } else {
                NativeCtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                        .padding(vertical = 12.Sdp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.Sdp)
                ) {
                    NativeIconContent()
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.Sdp)
                    ) {
                        NativeHeadlineRow()
                        NativeBodyText()
                    }
                }
            }
        }
    }
}

@Composable
fun NativeSmallCtaRight(
    nativeAdView: ApNativeAd,
    isMetaLow: Boolean = false,
    modifier: Modifier = Modifier
) {
    NativeAdView(nativeAd = nativeAdView, modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.Sdp, bottom = 10.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(4.8f)
                    .padding(start = 12.Sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NativeIconContent()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.Sdp)
                ) {
                    NativeHeadlineRow()
                    NativeBodyText()
                }
            }
            NativeCtaButton(
                modifier = Modifier
                    .weight(2.2f)
                    .padding(horizontal = 12.Sdp),
                isMetaLow = isMetaLow
            )
        }
    }
}


@Composable
fun NativeBannerCompose(
    nativeAdView: ApNativeAd,
    isMetaLow: Boolean = false,
    modifier: Modifier = Modifier
) {
    NativeAdView(nativeAd = nativeAdView, modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.Sdp, bottom = 10.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(4.8f)
                    .padding(start = 12.Sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NativeIconContent()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.Sdp)
                ) {
                    NativeHeadlineRow()
                    NativeBodyText()
                }
            }
            NativeCtaButton(
                modifier = Modifier
                    .weight(2.2f)
                    .padding(horizontal = 12.Sdp),
                isMetaLow = isMetaLow
            )
        }
    }
}

@Composable
fun ShimmerSmallCtaBotView(isShowShimmer: Boolean, modifier: Modifier = Modifier) {
    val shimmerState = rememberShimmerState(900)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(
                state = shimmerState,
                visible = isShowShimmer,
                highlightColor = NativeAdColors.shimmerHighlight
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.Sdp)
                .padding(top = 12.Sdp)
        ) {
            ShimmerIconCircle()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.Sdp)
            ) {
                ShimmerHeadlineRow()
                ShimmerBodyBox()
            }
        }
        ShimmerCtaRoundedButton(
            modifier = Modifier
                .padding(horizontal = 12.Sdp)
                .padding(top = 10.Sdp)
                .padding(bottom = 12.Sdp)
        )
    }
}

@Composable
fun ShimmerSmallCtaTopView(isShowShimmer: Boolean, modifier: Modifier = Modifier) {
    val shimmerState = rememberShimmerState(900)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(
                state = shimmerState,
                visible = isShowShimmer,
                highlightColor = NativeAdColors.shimmerHighlight
            )
    ) {
        ShimmerCtaRoundedButton(
            modifier = Modifier
                .padding(horizontal = 12.Sdp)
                .padding(vertical = 12.Sdp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.Sdp)
                .padding(bottom = 10.Sdp)
        ) {
            ShimmerIconCircle()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.Sdp)
            ) {
                ShimmerHeadlineRow()
                ShimmerBodyBox()
            }
        }
    }
}

@Composable
fun ShimmerSmallCtaRightView(isShowShimmer: Boolean, modifier: Modifier = Modifier) {
    val shimmerState = rememberShimmerState(900)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shimmer(
                state = shimmerState,
                visible = isShowShimmer,
                highlightColor = NativeAdColors.shimmerHighlight
            )
            .padding(top = 12.Sdp, bottom = 10.Sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(4.8f)
                .padding(start = 12.Sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerIconCircle()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.Sdp)
            ) {
                ShimmerHeadlineRow()
                ShimmerBodyBox()
            }
        }
        Box(
            modifier = Modifier
                .weight(2.2f)
                .padding(horizontal = 12.Sdp)
                .height(40.Sdp)
                .background(NativeAdColors.shimmer, RoundedCornerShape(0.dp))
        )
    }
}

@PhonePreview
@Composable
fun NativeSmallCtaBotPreview() {
    Column(
        Modifier
            .fillMaxSize()
            .background(lightGrey)
    ) {
        NativeSmallCtaBot(
            nativeAdView = ApNativeAd(), modifier = Modifier.background(
                white
            )
        )
        SpaceH(12.Sdp)
        NativeSmallCtaBot(
            nativeAdView = ApNativeAd(),
            isMeta = true,
            isMetaLow = true,
            modifier = Modifier.background(white)
        )
        SpaceH(12.Sdp)
        NativeSmallCtaBot(
            nativeAdView = ApNativeAd(),
            isMeta = true,
            isMetaLow = false,
            modifier = Modifier.background(white)
        )
    }
}

@PhonePreview
@Composable
fun NativeSmallCtaTopPreview() {
    Column(
        Modifier
            .fillMaxSize()
            .background(lightGrey)
    ) {
        NativeSmallCtaTop(
            nativeAdView = ApNativeAd(), modifier = Modifier.background(
                white
            )
        )
        SpaceH(12.Sdp)
        NativeSmallCtaTop(
            nativeAdView = ApNativeAd(),
            isMeta = true,
            isMetaLow = true,
            modifier = Modifier.background(white)
        )
        SpaceH(12.Sdp)
        NativeSmallCtaTop(
            nativeAdView = ApNativeAd(),
            isMeta = true,
            isMetaLow = false,
            modifier = Modifier.background(white)
        )
    }

}


@Preview(showBackground = true)
@Composable
fun NativeBannerPreview() {
    NativeBannerCompose(nativeAdView = ApNativeAd())
}

@Preview(showBackground = true)
@Composable
fun NativeSmallCtaRightPreview() {
    NativeSmallCtaRight(nativeAdView = ApNativeAd())
}

@Composable
fun ShimmerSmallCtaBotPreview() {
    ShimmerSmallCtaBotView(isShowShimmer = true)
}

@Preview(showBackground = true)
@Composable
fun ShimmerSmallCtaTopPreview() {
    ShimmerSmallCtaTopView(isShowShimmer = true)
}

@Preview(showBackground = true)
@Composable
fun ShimmerSmallCtaRightPreview() {
    ShimmerSmallCtaRightView(isShowShimmer = true)
}
