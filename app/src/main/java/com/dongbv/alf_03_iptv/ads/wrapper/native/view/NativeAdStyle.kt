package com.dongbv.alf_03_iptv.ads.wrapper.native.view

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.dongbv.alf_03_iptv.style.paddingHorizontal
import com.dongbv.alf_03_iptv.style.paddingTop
import com.dongbv.alf_03_iptv.style.paddingVertical
import com.dongbv.alf_03_iptv.widget.IptvPrimaryGradient
import com.lib.ads.gma.ads.helper.adnative.api.AdBadge
import com.lib.ads.gma.ads.helper.adnative.api.NativeAdBodyView
import com.lib.ads.gma.ads.helper.adnative.api.NativeAdButton
import com.lib.ads.gma.ads.helper.adnative.api.NativeAdCallToActionView
import com.lib.ads.gma.ads.helper.adnative.api.NativeAdHeadlineView
import com.lib.ads.gma.ads.helper.adnative.api.NativeAdIconView
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

object NativeAdColors {
    // Ad container background

    @ColorInt
    var colorBgNativeAds: Color = Color(0xFF1A1A2E)  // standard native
    val backgroundFull: Color = Color(0xFF1A1A2E)   // full-screen native

    // Text
    val headline: Color = Color(0xFFFFFFFF)
    val body: Color = Color(0xFF80828D)
    val sponsor: Color = Color(0xFF80828D)

    // "Ad" badge
    val badgeContainer: Color = Color(0xFF80828D)
    val badgeContent: Color = Color(0xFFFFFFFF)

    // CTA – default
    val ctaBrush: Brush = Brush.linearGradient(colors = listOf(Color(0xFF6366F1), Color(0xFF00F5FF)))
    val ctaContent: Color = Color.White

    // CTA – Meta low-CTR variant
    val ctaBrushLow: Brush = Brush.linearGradient(colors = listOf(Color(0xFFC1C1FF), Color(
        0xFFABFAFF
    )
    ))
    val ctaContentMetaLow: Color = Color(0xFF00CCDD)

    // Shimmer placeholder blocks
    val shimmer: Brush = Brush.linearGradient(colors = listOf(Color(0xFF3D3D3D), Color(0xFF3D3D3D)))
    var shimmerHighlight: Color = Color(0x4D3A3A3C)
}

// ── Icon ─────────────────────────────────────────────────────────────────────

@Composable
fun NativeIconContent(modifier: Modifier = Modifier) {
    NativeAdIconView(
        modifier = modifier.size(
            40.Sdp
        )
    ) { icon ->
        AndroidView(
            factory = { context ->
                ImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
            },
            update = { it.setImageDrawable(icon) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

// ── Headline rows ─────────────────────────────────────────────────────────────

/** AdBadge + headline at 14.Sdp — used by Small and Medium variants */
@Composable
fun NativeHeadlineRow(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        AdBadge(
            modifier = Modifier
                .background(
                    color = NativeAdColors.badgeContainer,
                    RoundedCornerShape(2.dp)
                )
                .paddingHorizontal(4.dp)
                .paddingVertical(2.dp), textStyle = TextStyle(
                color = NativeAdColors.badgeContent
            )
        )
        NativeAdHeadlineView(
            modifier = Modifier
                .padding(start = 10.Sdp)
                .weight(1f)
        ) { headline ->
            Text(
                text = headline,
                style = TextStyle(
                    color = NativeAdColors.headline,
                    fontSize = 12.Ssp,
                    fontWeight = FontWeight.W500
                ),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** AdBadge + headline at 11.Ssp — used by MediaLeft variants */
@Composable
fun NativeHeadlineRowSmall(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        AdBadge(
            modifier = Modifier
                .background(
                    color = NativeAdColors.badgeContainer,
                    RoundedCornerShape(2.dp)
                )
                .paddingHorizontal(4.dp)
                .paddingVertical(2.dp), textStyle = TextStyle(
                color = NativeAdColors.badgeContent
            )
        )
        NativeAdHeadlineView(
            modifier = Modifier.padding(
                start = 8.Sdp
            )
        ) { headline ->
            Text(
                text = headline,
                style = TextStyle(
                    color = NativeAdColors.headline,
                    fontSize = 11.Ssp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ── Body ──────────────────────────────────────────────────────────────────────

@Composable
fun NativeBodyText(modifier: Modifier = Modifier) {
    NativeAdBodyView(
        modifier = modifier.padding(
            top = 5.dp
        )
    ) { body ->
        Text(
            text = body,
            style = TextStyle(
                color = NativeAdColors.body,
                fontSize = 11.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NativeCtaButton(modifier: Modifier = Modifier, isMetaLow: Boolean = false) {
    val isPreview = LocalInspectionMode.current
    NativeAdCallToActionView(modifier = modifier) { cta ->
        val ctaText = if (isPreview) "Install" else cta
        NativeAdButton(
            text = ctaText,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            containerBrush = if (isMetaLow) NativeAdColors.ctaBrushLow else NativeAdColors.ctaBrush,
            contentColor = if (isMetaLow) NativeAdColors.ctaContentMetaLow else NativeAdColors.ctaContent,
            padding = PaddingValues(vertical = 10.Sdp, horizontal = 2.Sdp),
            textStyle = TextStyle(fontSize = 12.Ssp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun MetaTextSponsor(modifier: Modifier = Modifier) {
    Text(
        text = "Sponsor",
        style = TextStyle(color = NativeAdColors.sponsor, fontSize = 11.sp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.paddingTop(4.Sdp)
    )
}

@Composable
fun ShimmerIconCircle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(40.Sdp)
            .background(NativeAdColors.shimmer)
    )
}

@Composable
fun ShimmerHeadlineRow(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .background(NativeAdColors.shimmer, RoundedCornerShape(6.dp))
                .size(24.dp, 14.dp)
        )
        Box(
            modifier = Modifier
                .padding(start = 10.Sdp)
                .fillMaxWidth()
                .height(14.dp)
                .background(NativeAdColors.shimmer)
        )
    }
}

@Composable
fun ShimmerHeadlineRowSmall(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .background(NativeAdColors.shimmer, RectangleShape)
                .size(24.dp, 14.dp)
        )
        Box(
            modifier = Modifier
                .padding(start = 8.Sdp)
                .fillMaxWidth()
                .height(15.dp)
                .background(NativeAdColors.shimmer)
        )
    }
}

@Composable
fun ShimmerBodyBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(top = 8.Sdp)
            .fillMaxWidth()
            .height(24.dp)
            .background(NativeAdColors.shimmer)
    )
}

@Composable
fun ShimmerCtaRoundedButton(modifier: Modifier = Modifier) {
    ShimmerCtaButton(modifier)
}

@Composable
private fun ShimmerCtaButton(
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 40.Sdp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(NativeAdColors.shimmer, RectangleShape)
    )
}

@Composable
fun ShimmerCtaCircleButton(
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 40.Sdp
) {
    ShimmerCtaButton(modifier, height)
}
