package com.dongbv.alf_03_iptv.style

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ViewParentContent(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    onBack: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    BackHandler(onBack = {
        softwareKeyboardController?.hide()
        coroutineScope.launch {
            onBack.invoke()
        }
    })
    Box(
        modifier = modifier
            .fillMaxSize(1f),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        content = content
    )
}
@Composable
fun HorScrollView(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Box(
        modifier
            .horizontalScroll(scrollState)
            .fillMaxSize(), content = content
    )
}
@Composable
fun VerScrollView(modifier: Modifier, content: @Composable ColumnScope.() -> Unit) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Column (modifier.verticalScroll(scrollState)) {
        content()
    }
}
@Stable
fun Modifier.onClick(enable: Boolean =true,onClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enable,
        onClick = onClick
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.onLongClick(onLongClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }

    this.combinedClickable(
        onClick = {
        },
        onLongClick = {
            onLongClick()
        },
        indication = null,
        interactionSource = interactionSource
    )
}

enum class GradientType {
    Linear, Horizontal, Vertical, Radial, Sweep
}

fun Modifier.gradientContent(
    colors: List<Color> = listOf(Color(0xFFFFFFFF), Color(0xFF000000)),
    type: GradientType = GradientType.Linear
): Modifier = this.then(
    Modifier
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithCache {
            val gradient = when (type) {
                GradientType.Linear -> Brush.linearGradient(
                    colors = colors,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
                GradientType.Horizontal -> Brush.horizontalGradient(colors)
                GradientType.Vertical -> Brush.verticalGradient(colors)
                GradientType.Radial -> Brush.radialGradient(
                    colors = colors,
                    center = Offset(size.width / 2, size.height / 2),
                    radius = size.minDimension / 2
                )
                GradientType.Sweep -> Brush.sweepGradient(
                    colors = colors,
                    center = Offset(size.width / 2, size.height / 2)
                )
            }

            onDrawWithContent {
                drawContent()
                drawRect(brush = gradient, blendMode = BlendMode.SrcIn)
            }
        }
)
@Composable
fun Modifier.dashedBorder(
    strokeWidth: Dp,
    color: Color,
    cornerRadius: Dp = 0.dp,
    dashLength: Float = 10f,
    gapLength: Float = 10f
): Modifier = this.then(
    Modifier.drawBehind {
        val stroke = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)
        )
        val width = size.width
        val height = size.height
        val corner = cornerRadius.toPx()

        drawRoundRect(
            color = color,
            size = Size(width, height),
            style = stroke,
            cornerRadius = CornerRadius(corner, corner)
        )
    }
)
@Composable
fun Modifier.shimmer(
    visible: Boolean,
    alpha: Float = 1f,
    shape: Shape = RectangleShape,
    baseColor: Color = Color.LightGray.copy(alpha = 0.5f),
    highlightColor: Color = Color.LightGray.copy(alpha = 0.18f),
    durationMillis: Int = 900,
    angleDegrees: Float = 20f,
    bandWidthFraction: Float = 0.45f
): Modifier = composed {
    if (!visible) return@composed this
    val transition = rememberInfiniteTransition(label = "shimmer")
    val anim by transition.animateFloat(
        initialValue = -1f, targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset"
    )
    val (dx, dy) = remember(angleDegrees) {
        val r = Math.toRadians(angleDegrees.toDouble())
        kotlin.math.cos(r).toFloat() to kotlin.math.sin(r).toFloat()
    }

    drawWithCache {
        if (size.minDimension <= 0f || alpha <= 0f) {
            onDrawWithContent { drawContent() }
        } else {
            val w = size.width
            val band = (bandWidthFraction.coerceIn(0.1f, 0.5f)) * w
            val outline = shape.createOutline(size, layoutDirection, this)
            val cachedPath: androidx.compose.ui.graphics.Path? =
                when (outline) {
                    is Outline.Generic -> outline.path
                    is Outline.Rounded -> androidx.compose.ui.graphics.Path().apply {
                        addRoundRect(outline.roundRect)
                    }
                    is Outline.Rectangle -> null
                }
            val baseA = baseColor.copy(alpha = baseColor.alpha * alpha)
            val hiA = highlightColor.copy(alpha = highlightColor.alpha * alpha)
            val colors = listOf(baseA, hiA, baseA)

            onDrawWithContent {
                drawContent()
                val startX = -band + anim * (w + band * 2)
                val start = Offset(startX * dx, startX * dy)
                val end = Offset(start.x + band * dx, start.y + band * dy)
                val brush = Brush.linearGradient(colors = colors, start = start, end = end)
                if (cachedPath != null) {
                    drawPath(path = cachedPath, brush = brush)
                } else {
                    drawRect(brush = brush)
                }
            }
        }
    }
}
@Composable
private inline fun Modifier.drawEdgeLine(
    width: Dp,
    color: Color?,
    brush: Brush?,
    crossinline computeStartEnd: androidx.compose.ui.graphics.drawscope.DrawScope.(strokePx: Float) -> Pair<Offset, Offset>
): Modifier = drawWithContent {
    drawContent()
    val strokePx = width.toPx()
    if (strokePx <= 0f) return@drawWithContent
    val (start, end) = computeStartEnd(strokePx)
    when {
        brush != null -> drawLine(
            brush = brush,
            start = start,
            end = end,
            strokeWidth = strokePx
        )
        color != null && color != Color.Unspecified -> drawLine(
            color = color,
            start = start,
            end = end,
            strokeWidth = strokePx
        )
    }
}
@Composable
fun Modifier.borderTop(
    width: Dp = _1Sdp,
    color: Color? = Color.Black,
    brush: Brush? = null
): Modifier = composed {
    this.then(
        Modifier.drawEdgeLine(width, color, brush) { strokePx ->
            val y = strokePx / 2f
            Offset(0f, y) to Offset(size.width, y)
        }
    )
}
@Composable
fun Modifier.borderBottom(
    width: Dp = _1Sdp,
    color: Color? = Color.Black,
    brush: Brush? = null
): Modifier = composed {
    this.then(
        Modifier.drawEdgeLine(width, color, brush) { strokePx ->
            val y = size.height - strokePx / 2f
            Offset(0f, y) to Offset(size.width, y)
        }
    )
}
@Composable
fun Modifier.borderStart(
    width: Dp = _1Sdp,
    color: Color? = Color.Black,
    brush: Brush? = null
): Modifier = composed {
    val layoutDir = LocalLayoutDirection.current
    this.then(
        Modifier.drawEdgeLine(width, color, brush) { strokePx ->
            val x =
                if (layoutDir == LayoutDirection.Ltr) strokePx / 2f else size.width - strokePx / 2f
            Offset(x, 0f) to Offset(x, size.height)
        }
    )
}
@Composable
fun Modifier.borderEnd(
    width: Dp = _1Sdp,
    color: Color? = Color.Black,
    brush: Brush? = null
): Modifier = composed {
    val layoutDir = LocalLayoutDirection.current
    this.then(
        Modifier.drawEdgeLine(width, color, brush) { strokePx ->
            val x =
                if (layoutDir == LayoutDirection.Ltr) size.width - strokePx / 2f else strokePx / 2f
            Offset(x, 0f) to Offset(x, size.height)
        }
    )
}
@Composable
fun Modifier.borderHorizontal(
    width: Dp = _1Sdp,
    color: Color? = Color.Black,
    brush: Brush? = null
): Modifier = this
    .borderTop(width, color, brush)
    .borderBottom(width, color, brush)
@Composable
fun Modifier.borderVertical(
    width: Dp = _1Sdp,
    color: Color? = Color.Black,
    brush: Brush? = null
): Modifier = this
    .borderStart(width, color, brush)
    .borderEnd(width, color, brush)
@Composable
fun DotIndicator(count: Int = 3, current: Int = 0) {
    val activeDotSize = _16Sdp
    val inactiveDotSize = _8Sdp
    val activeDotColor = Color.Blue
    val inactiveDotColor = Color.Gray
    Row(
        horizontalArrangement = Arrangement.spacedBy(_8Sdp),
        modifier = Modifier.padding(horizontal = _16Sdp, vertical = _4Sdp)
    ) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .width(if (i == current) activeDotSize else inactiveDotSize)
                    .height(inactiveDotSize)
                    .background(
                        color = if (i == current) activeDotColor else inactiveDotColor,
                        shape = CircleShape
                    )
            )
        }
    }
}
@Composable
fun CustomSwitch(
    checked: Boolean,
    thumbOn: Color = Color.Blue,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val switchWidth = _46Sdp
    val switchHeight = _24Sdp
    val thumbRadius = _12Sdp
    val thumbPadding = _2Sdp
    val thumbPosition = animateDpAsState(
        targetValue = if (checked) switchWidth - thumbRadius * 1.8f - thumbPadding else thumbPadding,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = modifier
            .size(switchWidth, switchHeight)
            .clip(RoundedCornerShape(switchHeight / 2))
            .background(if (checked) thumbOn else Color(0xFFB5B5B5))
            .onClick { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbRadius * 1.8f)
                .padding(thumbPadding)
                .offset(x = thumbPosition.value)
                .background(Color.White, CircleShape)
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicator(pagerState: PagerState, count: Int = pagerState.pageCount) {
    val activeDotSize = _10Sdp
    val inactiveDotSize = _10Sdp
    val activeDotColor = Color.Blue
    val inactiveDotColor = Color(0xffC5C5C5)

    Row(
        horizontalArrangement = Arrangement.spacedBy(_8Sdp),
        modifier = Modifier.padding(vertical = _2Sdp)
    ) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .width(if (i == pagerState.currentPage) activeDotSize else inactiveDotSize)
                    .height(inactiveDotSize)
                    .background(
                        color = if (i == pagerState.currentPage) activeDotColor else inactiveDotColor,
                        shape = CircleShape
                    )
            )
        }
    }
}
@Composable
fun IconView(
    item: Any,
    tint: Color? = null,
    icSize: Int = 24,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onclick: (() -> Unit)? = null
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (item) {
            is ImageVector -> Icon(
                item,
                contentDescription = "Image Vector",
                tint = tint ?: Color.Unspecified,
                modifier = Modifier.size(icSize.dp)
            )
            is Painter -> Icon(
                painter = item,
                contentDescription = "Painter",
                modifier = Modifier.size(icSize.dp),
                tint = tint ?: Color.Unspecified
            )
            is Int ->
                Icon(
                    painter = painterResource(item),
                    contentDescription = "Painter",
                    modifier = Modifier.size(icSize.dp),
                    tint = tint ?: Color.Unspecified
                )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .onClick { onclick?.invoke() })
    }
}


















































