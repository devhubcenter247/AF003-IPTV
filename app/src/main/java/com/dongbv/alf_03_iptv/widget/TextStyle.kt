package com.dongbv.alf_03_iptv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.style._12Ssp
import com.dongbv.alf_03_iptv.style.black

enum class GradientOrientation {
    Horizontal,
    Vertical,
    TL_BR,
    TR_BL,
    BL_TR,
    BR_TL,
    RadialCenter,
    SweepCenter,
}

private val INF = Float.POSITIVE_INFINITY
fun gradientBrush(
    orientation: GradientOrientation = GradientOrientation.Horizontal,
    colors: List<Color> = listOf(Color(0xFF454BD0), Color(0xFF5F66F3)),
    isRevertColor: Boolean = false,
): Brush {
    val c = if (isRevertColor) colors.reversed() else colors
    return when (orientation) {
        GradientOrientation.Horizontal -> Brush.horizontalGradient(c)
        GradientOrientation.Vertical -> Brush.verticalGradient(c)
        GradientOrientation.TL_BR -> Brush.linearGradient(
            c,
            start = Offset(0f, 0f),
            end = Offset(INF, INF)
        )

        GradientOrientation.TR_BL -> Brush.linearGradient(
            c,
            start = Offset(INF, 0f),
            end = Offset(0f, INF)
        )

        GradientOrientation.BL_TR -> Brush.linearGradient(
            c,
            start = Offset(0f, INF),
            end = Offset(INF, 0f)
        )

        GradientOrientation.BR_TL -> Brush.linearGradient(
            c,
            start = Offset(INF, INF),
            end = Offset(0f, 0f)
        )

        GradientOrientation.RadialCenter -> Brush.radialGradient(c)
        GradientOrientation.SweepCenter -> Brush.sweepGradient(c)
    }
}

@Composable
fun headline400(color: Color = black, fontSize: TextUnit = _12Ssp) = TextStyle(
    fontFamily = FontFamily(Font(R.font.ss_400)),
    fontSize = fontSize,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = color
)

@Composable
fun headline500(color: Color = black, fontSize: TextUnit = _12Ssp) = TextStyle(
    fontFamily = FontFamily(Font(R.font.ss_500)),
    fontSize = fontSize,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = color
)
@Composable
fun headline700(color: Color = black, fontSize: TextUnit = _12Ssp) = TextStyle(
    fontFamily = FontFamily(Font(R.font.ss_700)),
    fontSize = fontSize,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = color
)
@Composable
fun headline600(color: Color = black, fontSize: TextUnit = _12Ssp) = TextStyle(
    fontFamily = FontFamily(Font(R.font.ss_600)),
    fontSize = fontSize,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    color = color
)
