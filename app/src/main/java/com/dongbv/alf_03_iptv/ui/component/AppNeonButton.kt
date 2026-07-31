package com.dongbv.alf_03_iptv.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp

object NeonPalettes {

    val Blue = listOf(
        Color(0xFF00C6FF),
        Color(0xFF0066FF)
    )

    val Green = listOf(
        Color(0xFF00F5A0),
        Color(0xFF00B85C)
    )

    val Purple = listOf(
        Color(0xFF8B5CF6),
        Color(0xFF6D28D9)
    )

    val Pink = listOf(
        Color(0xFFFF4D94),
        Color(0xFFDB0075)
    )

    val Orange = listOf(
        Color(0xFFFFB000),
        Color(0xFFFF4D00)
    )

    val Cyan = listOf(
        Color(0xFF22D3EE),
        Color(0xFF0284C7)
    )

    val Cyber = listOf(
        Color(0xFF00F5FF),
        Color(0xFF6366F1)
    )
}


@Composable
fun AppNeonButton(
    text: String,
    onClick: () -> Unit = {},
    textStyle: TextStyle = TextStyle.Default.copy(fontSize = 12.Ssp),
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(grdStart, grdEnd),
    shape: RoundedCornerShape = RoundedCornerShape(24.Sdp),
    enabled: Boolean = true,
    neonEnabled: Boolean = true
) {
    require(colors.size >= 2)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (enabled && isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label = "button_scale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = when {
            !neonEnabled -> 0f
            !enabled -> 0.1f
            isPressed -> 0.35f
            else -> 0.72f
        },
        label = "button_glow"
    )

    val displayedColors = if (enabled) {
        colors
    } else {
        listOf(Color.White, Color.White).map { it.copy(alpha = 0.1f) }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (neonEnabled) {
                    Modifier
                        .dropShadow(
                            shape = shape,
                            shadow = Shadow(
                                radius = 16.dp,
                                spread = 1.dp,
                                brush = Brush.horizontalGradient(colors),
                                alpha = glowAlpha
                            )
                        )
                        .dropShadow(
                            shape = shape,
                            shadow = Shadow(
                                radius = 6.dp,
                                spread = 1.dp,
                                color = colors.last(),
                                alpha = glowAlpha
                            )
                        )
                } else {
                    Modifier
                }
            )
            .clip(shape)
            .background(Brush.horizontalGradient(displayedColors))
            .border(
                width = 0.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color.White.copy(alpha = if (neonEnabled) 0.65f else 0f),
                        Color.Transparent,
                        if(enabled){
                            colors.last().copy(alpha = if (neonEnabled) 0.8f else 0.1f)
                        } else {
                            Color.White.copy(alpha = 0f)
                        }

                    )
                ),
                shape = shape
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .defaultMinSize(minHeight = 52.dp)
            .padding(horizontal = 28.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White.copy(alpha = if (enabled) 1f else 0.45f),
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}

@Preview(device = PIXEL_4_XL, showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun PreviewUnitNeonButton() {
    var value by remember { mutableStateOf("Error dsjksjdksjd") }
    val isError = value.length > 10
    AppViewTheme {
        Column(Modifier.fillMaxWidth()) {
            AppNeonButton(
             text =    "Click me"
            )
            AppNeonButton(
                text =    "Click me",
                colors = NeonPalettes.Green,
            )
            AppNeonButton(
                text =    "Click me",
                colors = NeonPalettes.Purple,
            )
            AppNeonButton(
                text =    "Click me",
                colors = NeonPalettes.Pink,
            )
            AppNeonButton(
                text =    "Click me",
                colors = NeonPalettes.Orange,
            )
            AppNeonButton(
                text =    "Click me",
                colors = NeonPalettes.Cyan,
            )
            AppNeonButton(
                text =    "Click me",
                colors = NeonPalettes.Cyber
            )
            SpaceH(16.dp)
            AppNeonButton(
                text =    "Click me",
                enabled = false
            )
            SpaceH(16.dp)
            AppNeonButton(
                text =    "Click me",
                enabled = false,
                colors = NeonPalettes.Blue
            )

        }
    }
}
