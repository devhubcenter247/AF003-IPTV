package com.dongbv.alf_03_iptv.ui.component


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.style.opacity
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.widget.IptvPrimaryGradient

@Stable
fun Modifier.borderState(
    enabled: Boolean = true,
    width: Dp = 1.dp,
    shape: Shape = CircleShape,
    enableColors: List<Color> = listOf(white.opacity(0.5f), white.opacity(0.5f)),
    disableColors: List<Color> = listOf(white.opacity(0.1f), white.opacity(0.1f))
) = this.border(
    width = width,
    shape = shape,
    brush = Brush.horizontalGradient(
        if (enabled) enableColors else disableColors
    )
)

@Stable
fun Modifier.backgroundState(
    enabled: Boolean = true,
    shape: Shape = CircleShape,
    enableColors: List<Color> = IptvPrimaryGradient,
    disableColors: List<Color> = listOf(white.opacity(0.1f), white.opacity(0.1f))
) = this.background(
    shape = shape,
    brush = Brush.horizontalGradient(
        if (enabled) enableColors else disableColors
    )
)

@Stable
fun Modifier.neonState(
    neonEnabled: Boolean = true,
    alpha: Float = 0.72f,
    shape: Shape = CircleShape,
    colors: List<Color> = IptvPrimaryGradient,
    outerRadius: Dp = 16.dp,
    outerSpread: Dp = 1.dp,
    innerRadius: Dp = 0.dp,
    innerSpread: Dp = 0.dp
) = if (!neonEnabled) this else this
    .dropShadow(
        shape = shape,
        shadow = Shadow(
            radius = outerRadius,
            spread = outerSpread,
            brush = Brush.horizontalGradient(colors),
            alpha = alpha
        )
    )
    .dropShadow(
        shape = shape,
        shadow = Shadow(
            radius = innerRadius,
            spread = innerSpread,
            color = colors.last(),
            alpha = alpha
        )
    )
