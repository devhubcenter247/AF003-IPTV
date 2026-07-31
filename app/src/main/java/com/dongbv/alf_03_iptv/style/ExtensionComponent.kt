package com.dongbv.alf_03_iptv.style

import android.annotation.SuppressLint
import android.graphics.RuntimeShader
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import java.lang.Math.cos
import java.lang.Math.sin

@Composable
fun BoxParentContent(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    onBack: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    BackHandler(onBack = {
        softwareKeyboardController?.hide()
        onBack()
    })
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        content = content
    )
}

@Composable
fun RowParentContent(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    onBack: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    BackHandler(onBack = {
        softwareKeyboardController?.hide()
        onBack()
    })
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        content = content
    )
}

@Composable
fun ColumnParentContent(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    BackHandler(onBack = {
        softwareKeyboardController?.hide()
        onBack()
    })
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun ConstraintParentContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    content: @Composable ConstraintLayoutScope.() -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    BackHandler(onBack = {
        softwareKeyboardController?.hide()
        onBack()
    })
    ConstraintLayout(
        modifier = modifier.fillMaxSize(), content = content
    )
}

@Composable
fun HorScrollView(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val scrollState = rememberScrollState()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        if (keyboardHeight > 0) {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Box(
        modifier
            .fillMaxSize()
            .horizontalScroll(scrollState), content = content
    )
}

@Composable
fun VerScrollView(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val scrollState = rememberScrollState()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight) {
        if (keyboardHeight > 0) {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        content()
    }
}

@Stable
fun Modifier.onClick(enable: Boolean = true, onClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enable,
        onClick = onClick
    )
}
@Stable
fun Modifier.onClickRipple(
    shape: Shape = RoundedCornerShape(16.dp),
    enabled: Boolean = true,
    bounded: Boolean = true,
    onClick: () -> Unit
): Modifier = this
    .clip(shape)
    .clickable(
        interactionSource = null,
        indication = ripple(bounded = bounded),
        enabled = enabled,
        onClick = onClick
    )

@OptIn(ExperimentalFoundationApi::class)
@Stable
fun Modifier.onLongClick(onLongClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.combinedClickable(
        onClick = {}, onLongClick = {
            onLongClick()
        }, indication = null, interactionSource = interactionSource
    )
}

enum class GradientType {
    Linear, Horizontal, Vertical, Radial, Sweep
}

@Stable
fun Modifier.gradientContent(
    colors: List<Color> = listOf(Color(0xFFFFFFFF), Color(0xFF000000)),
    type: GradientType = GradientType.Linear
): Modifier =
    this.then(
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
                        colors = colors, center = Offset(size.width / 2, size.height / 2)
                    )
                }

                onDrawWithContent {
                    drawContent()
                    drawRect(brush = gradient, blendMode = BlendMode.SrcIn)
                }
            })

@Stable
fun Modifier.backgroundResource(
    @DrawableRes id: Int,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    alpha: Float = 1f
): Modifier = composed {
    this.paint(
        painter = painterResource(id),
        contentScale = contentScale,
        alignment = alignment,
        alpha = alpha
    )
}

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
    })

@Stable
class ShimmerState internal constructor(private val animatedProgress: State<Float>) {
    val progress: Float get() = animatedProgress.value
}

@Composable
fun rememberShimmerState(durationMillis: Int = 1200): ShimmerState {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val anim = transition.animateFloat(
        initialValue = -1f, targetValue = 2f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "offset"
    )
    return remember { ShimmerState(anim) }
}

@get:RequiresApi(33)
private val SHIMMER_AGSL_SRC = """
    uniform float2 resolution;
    uniform float  progress;
    uniform float4 baseColor;
    uniform float4 highColor;
    uniform float  band;
    uniform float  angleDx;
    uniform float  angleDy;

    half4 main(float2 coord) {
        float totalTravel = resolution.x + band * 2.0;
        float startX      = -band + progress * totalTravel;
        float2 start      = float2(startX * angleDx, startX * angleDy);
        float2 dir        = float2(angleDx, angleDy);
        float t           = dot(coord - start, dir) / band;
        float factor      = 1.0 - abs(clamp(t, 0.0, 1.0) - 0.5) * 2.0;
        return half4(mix(baseColor, highColor, factor));
    }
""".trimIndent()

@Stable
fun Modifier.shimmer(
    state: ShimmerState,
    visible: Boolean,
    alpha: Float = 1f,
    shape: Shape = RectangleShape,
    baseColor: Color = Color.LightGray.copy(alpha = 0f),
    highlightColor: Color = Color.LightGray.copy(alpha = 0.55f),
    angleDegrees: Float = 20f,
    bandWidthFraction: Float = 0.45f
): Modifier = this.then(
    if (!visible) Modifier
    else if (android.os.Build.VERSION.SDK_INT >= 33) {
        Modifier.shimmerAGSL(
            state,
            alpha,
            shape,
            baseColor,
            highlightColor,
            angleDegrees,
            bandWidthFraction
        )
    } else {
        Modifier.shimmerLegacy(
            state, alpha, shape, baseColor, highlightColor, angleDegrees, bandWidthFraction
        )
    }
)

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@RequiresApi(33)
private fun Modifier.shimmerAGSL(
    state: ShimmerState,
    alpha: Float,
    shape: Shape,
    baseColor: Color,
    highlightColor: Color,
    angleDegrees: Float,
    bandWidthFraction: Float
): Modifier = Modifier.drawWithCache {
    if (size.minDimension <= 0f || alpha <= 0f) {
        onDrawWithContent { drawContent() }
    } else {
        val angleRad = Math.toRadians(angleDegrees.toDouble())
        val dx = cos(angleRad).toFloat()
        val dy = sin(angleRad).toFloat()
        val band = bandWidthFraction.coerceIn(0.1f, 0.5f) * size.width
        val runtimeShader = RuntimeShader(SHIMMER_AGSL_SRC).apply {
            setFloatUniform("resolution", size.width, size.height)
            setFloatUniform("band", band)
            setFloatUniform("angleDx", dx)
            setFloatUniform("angleDy", dy)
            setFloatUniform(
                "baseColor", baseColor.red, baseColor.green, baseColor.blue, baseColor.alpha * alpha
            )
            setFloatUniform(
                "highColor",
                highlightColor.red,
                highlightColor.green,
                highlightColor.blue,
                highlightColor.alpha * alpha
            )
        }

        val paint = Paint().apply {
            isAntiAlias = true
            shader = runtimeShader
        }

        val cachedPath = shape.toCachedPath(size, layoutDirection)

        onDrawWithContent {
            drawContent()
            runtimeShader.setFloatUniform("progress", state.progress)
            drawIntoCanvas { canvas ->
                if (cachedPath != null) canvas.drawPath(cachedPath, paint)
                else canvas.drawRect(0f, 0f, size.width, size.height, paint)
            }
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.shimmerLegacy(
    state: ShimmerState,
    alpha: Float,
    shape: Shape,
    baseColor: Color,
    highlightColor: Color,
    angleDegrees: Float,
    bandWidthFraction: Float
): Modifier = Modifier.drawWithCache {
    if (size.minDimension <= 0f || alpha <= 0f) {
        onDrawWithContent { drawContent() }
    } else {
        val angleRad = Math.toRadians(angleDegrees.toDouble())
        val dx = cos(angleRad).toFloat()
        val dy = sin(angleRad).toFloat()
        val band = bandWidthFraction.coerceIn(0.1f, 0.5f) * size.width
        val totalTravel = size.width + band * 2f
        val originOffset = -band

        val baseA = baseColor.copy(alpha = baseColor.alpha * alpha)
        val hiA = highlightColor.copy(alpha = highlightColor.alpha * alpha)
        val colors = listOf(baseA, hiA, baseA)

        val paint = Paint().apply { isAntiAlias = true }
        val cachedPath = shape.toCachedPath(size, layoutDirection)

        onDrawWithContent {
            drawContent()
            val startX = originOffset + state.progress * totalTravel
            val start = Offset(startX * dx, startX * dy)
            val end = Offset(start.x + band * dx, start.y + band * dy)
            paint.shader = LinearGradientShader(
                from = start, to = end, colors = colors, tileMode = TileMode.Clamp
            )
            drawIntoCanvas { canvas ->
                if (cachedPath != null) canvas.drawPath(cachedPath, paint)
                else canvas.drawRect(0f, 0f, size.width, size.height, paint)
            }
        }
    }
}

private fun Shape.toCachedPath(
    size: androidx.compose.ui.geometry.Size, layoutDirection: LayoutDirection
): Path? {
    return when (val outline =
        createOutline(size, layoutDirection, androidx.compose.ui.unit.Density(1f))) {
        is Outline.Generic -> outline.path
        is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
        is Outline.Rectangle -> null
    }
}

@Stable
private inline fun Modifier.drawEdgeLine(
    width: Dp,
    color: Color?,
    brush: Brush?,
    crossinline computeStartEnd: DrawScope.(strokePx: Float) -> Pair<Offset, Offset>
): Modifier = drawWithContent {
    drawContent()
    val strokePx = width.toPx()
    if (strokePx <= 0f) return@drawWithContent
    val (start, end) = computeStartEnd(strokePx)
    when {
        brush != null -> drawLine(
            brush = brush, start = start, end = end, strokeWidth = strokePx
        )

        color != null && color != Color.Unspecified -> drawLine(
            color = color, start = start, end = end, strokeWidth = strokePx
        )
    }
}

@Stable
fun Modifier.borderTop(
    width: Dp = 1.5.dp, color: Color? = Color.Black, brush: Brush? = null
): Modifier = this.then(
    Modifier.drawEdgeLine(width, color, brush) { strokePx ->
        val y = strokePx / 2f
        Offset(0f, y) to Offset(size.width, y)
    })


@Stable
fun Modifier.borderBottom(
    width: Dp = 1.5.dp, color: Color? = Color.Black, brush: Brush? = null
): Modifier =   this.then(
        Modifier.drawEdgeLine(width, color, brush) { strokePx ->
            val y = size.height - strokePx / 2f
            Offset(0f, y) to Offset(size.width, y)
        })

@SuppressLint("UnnecessaryComposedModifier")
@Stable
fun Modifier.borderStart(
    width: Dp = 1.5.dp, color: Color? = Color.Black, brush: Brush? = null
): Modifier = composed {
    val layoutDir = LocalLayoutDirection.current
    this.then(
        Modifier.drawEdgeLine(width, color, brush) { strokePx ->
            val x =
                if (layoutDir == LayoutDirection.Ltr) strokePx / 2f else size.width - strokePx / 2f
            Offset(x, 0f) to Offset(x, size.height)
        })
}

@Composable
fun Modifier.borderEnd(
    width: Dp = 1.5.dp, color: Color? = Color.Black, brush: Brush? = null
): Modifier = composed {
    val layoutDir = LocalLayoutDirection.current
    this.then(
        Modifier.drawEdgeLine(width, color, brush) { strokePx ->
            val x =
                if (layoutDir == LayoutDirection.Ltr) size.width - strokePx / 2f else strokePx / 2f
            Offset(x, 0f) to Offset(x, size.height)
        })
}

@Composable
fun Modifier.borderHorizontal(
    width: Dp = 1.5.dp, color: Color? = Color.Black, brush: Brush? = null
): Modifier = this
    .borderTop(width, color, brush)
    .borderBottom(width, color, brush)

@Composable
fun Modifier.borderVertical(
    width: Dp = 1.5.dp, color: Color? = Color.Black, brush: Brush? = null
): Modifier = this
    .borderStart(width, color, brush)
    .borderEnd(width, color, brush)

@Composable
fun CustomSwitch(
    checked: Boolean,
    thumbOn: Color = Color.Blue,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.onClick { onCheckedChange(!checked) }) {
        val width = if (constraints.hasBoundedWidth) maxWidth else 52.dp
        val height = if (constraints.hasBoundedHeight) maxHeight else 28.dp

        val thumbPadding = height * 0.1f
        val thumbSize = height - (thumbPadding * 2)

        val thumbPosition by animateDpAsState(
            targetValue = if (checked) width - thumbSize - thumbPadding else thumbPadding,
            animationSpec = tween(durationMillis = 300),
            label = ""
        )

        Box(
            modifier = Modifier
                .size(width, height)
                .clip(CircleShape)
                .background(if (checked) thumbOn else Color(0xFFB5B5B5)),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .offset(x = thumbPosition)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

@Composable
fun Modifier.onAppear(block: () -> Unit): Modifier {
    LaunchedEffect(Unit) { block() }
    return this
}

@Composable
fun Modifier.onDisappear(block: () -> Unit): Modifier {
    DisposableEffect(Unit) { onDispose { block() } }
    return this
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientCircularProgressIndicator(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(Color(0xFF6650A4), Color(0xFF03DAC6)),
    tailFraction: Float = 0.15f,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    trackColor: Color = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
) {
    val density = LocalDensity.current
    val stroke = remember(strokeWidth, strokeCap, density) {
        Stroke(width = with(density) { strokeWidth.toPx() }, cap = strokeCap)
    }
    val sizePx = remember(density) { with(density) { 40.dp.toPx() } }
    // Cached once per color change — never recreated inside the draw loop.
    // Position 0 = transparent tail; user colors are distributed from 15% onward
    // so the trailing edge always fades out while the leading edge stays vivid.
    val gradientBrush = remember(colors, sizePx, tailFraction) {
        val tailEnd = tailFraction.coerceIn(0f, 0.99f)
        val stops = buildList {
            add(0f to colors.first().copy(alpha = 0f))
            val last = (colors.size - 1).coerceAtLeast(1)
            colors.forEachIndexed { i, color ->
                add((tailEnd + (1f - tailEnd) * i / last) to color)
            }
        }
        Brush.sweepGradient(
            colorStops = stops.toTypedArray(),
            center = Offset(sizePx / 2, sizePx / 2)
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "gradient_circular")

    // Seamless: 360° == 0°, so Restart never jumps visually
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Reverse = ping-pong → no hard jump at cycle boundary
    val sweepFraction by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sweep"
    )

    Canvas(modifier.size(40.dp)) {
        val sweep = sweepFraction * 360f
        val diameterOffset = stroke.width / 2
        val arcDimen = size.width - 2 * diameterOffset
        val topLeft = Offset(diameterOffset, diameterOffset)
        val arcSize = Size(arcDimen, arcDimen)

        rotate(rotation) {
            drawArc(
                color = trackColor,
                startAngle = sweep,
                sweepAngle = 360f - sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke
            )
            drawArc(
                brush = gradientBrush,
                startAngle = 0f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientLinearProgressIndicator(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(Color(0xFF6650A4), Color(0xFF03DAC6)),
    tailFraction: Float = 0.3f,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    gapSize: Dp = ProgressIndicatorDefaults.LinearIndicatorTrackGapSize,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_linear")

    val firstLineHead by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 0 using CubicBezierEasing(0.2f, 0f, 0.8f, 1f)
                1f at 750; 1f at 1800
            }, repeatMode = RepeatMode.Restart
        ), label = "fh"
    )
    val firstLineTail by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 0; 0f at 333 using CubicBezierEasing(0.4f, 0f, 1f, 1f)
                1f at 1166; 1f at 1800
            }, repeatMode = RepeatMode.Restart
        ), label = "ft"
    )
    val secondLineHead by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 0; 0f at 1000 using CubicBezierEasing(0f, 0f, 0.65f, 1f)
                1f at 1567; 1f at 1800
            }, repeatMode = RepeatMode.Restart
        ), label = "sh"
    )
    val secondLineTail by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0f at 0; 0f at 1267 using CubicBezierEasing(0.4f, 0f, 0.6f, 1f)
                1f at 1800
            }, repeatMode = RepeatMode.Restart
        ), label = "st"
    )

    Canvas(modifier.fillMaxWidth().height(4.dp)) {
        val strokeWidth = size.height
        val width = size.width
        val yOffset = size.height / 2
        val adjustedGapSize = if (strokeCap == StrokeCap.Butt || size.height > width) {
            gapSize
        } else {
            gapSize + strokeWidth.toDp()
        }
        val gapSizeFraction = adjustedGapSize / size.width.toDp()
        val fade = tailFraction.coerceIn(0f, 1f)
        val last = (colors.size - 1).coerceAtLeast(1)

        fun drawTrack(startF: Float, endF: Float) {
            val x1 = startF * width
            val x2 = endF * width
            if (strokeCap == StrokeCap.Butt || size.height > width) {
                drawLine(trackColor, Offset(x1, yOffset), Offset(x2, yOffset), strokeWidth)
            } else {
                val cap = strokeWidth / 2
                val a = x1.coerceIn(cap, width - cap)
                val b = x2.coerceIn(cap, width - cap)
                if (b - a > 0f) drawLine(trackColor, Offset(a, yOffset), Offset(b, yOffset), strokeWidth, cap = strokeCap)
            }
        }

        fun drawGradientLine(headF: Float, tailF: Float) {
            val headX = headF * width
            val tailX = tailF * width
            val lineLen = headX - tailX
            if (lineLen <= 0f) return
            val fadeEndX = tailX + lineLen * fade

            // Build color stops: transparent at tail → first color at fadeEnd → … → last color at head
            val stops = Array(colors.size + 1) { i ->
                when (i) {
                    0 -> tailX / width to colors.first().copy(alpha = 0f)
                    1 -> fadeEndX / width to colors.first()
                    else -> {
                        val idx = i - 1
                        val x = fadeEndX + (headX - fadeEndX) * idx / last
                        x / width to colors[idx.coerceAtMost(colors.size - 1)]
                    }
                }
            }
            val brush = Brush.horizontalGradient(*stops, startX = 0f, endX = width)

            if (strokeCap == StrokeCap.Butt || size.height > width) {
                drawLine(brush, Offset(tailX, yOffset), Offset(headX, yOffset), strokeWidth)
            } else {
                val cap = strokeWidth / 2
                val a = tailX.coerceIn(cap, width - cap)
                val b = headX.coerceIn(cap, width - cap)
                if (b - a > 0f) drawLine(brush, Offset(a, yOffset), Offset(b, yOffset), strokeWidth, cap = strokeCap)
            }
        }

        // Track before line 1
        if (firstLineHead < 1f - gapSizeFraction) {
            val start = if (firstLineHead > 0f) firstLineHead + gapSizeFraction else 0f
            drawTrack(start, 1f)
        }
        // Line 1
        if (firstLineHead - firstLineTail > 0f) drawGradientLine(firstLineHead, firstLineTail)
        // Track between lines
        if (firstLineTail > gapSizeFraction) {
            val start = if (secondLineHead > 0f) secondLineHead + gapSizeFraction else 0f
            val end = if (firstLineTail < 1f) firstLineTail - gapSizeFraction else 1f
            drawTrack(start, end)
        }
        // Line 2
        if (secondLineHead - secondLineTail > 0f) drawGradientLine(secondLineHead, secondLineTail)
        // Track after line 2
        if (secondLineTail > gapSizeFraction) {
            val end = if (secondLineTail < 1f) secondLineTail - gapSizeFraction else 1f
            drawTrack(0f, end)
        }
    }
}

@Composable
fun IconView(
    item: Any,
    tint: Color? = null,
    icSize: Int = 24,
    mirrorForRtl: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onclick: (() -> Unit)? = null
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val iconModifier = Modifier
        .size(icSize.dp)
        .then(if (mirrorForRtl && isRtl) Modifier.graphicsLayer { scaleX = -1f } else Modifier)
    Box(modifier = modifier.then(if (onclick != null) Modifier.onClick { onclick() } else Modifier),
        contentAlignment = Alignment.Center) {
        when (item) {
            is ImageVector -> Icon(
                item,
                contentDescription = "Image Vector",
                tint = tint ?: Color.Unspecified,
                modifier = iconModifier
            )

            is Painter -> Icon(
                painter = item,
                contentDescription = "Painter",
                modifier = iconModifier,
                tint = tint ?: Color.Unspecified
            )

            is Int -> Icon(
                painter = painterResource(item),
                contentDescription = "Painter",
                modifier = iconModifier,
                tint = tint ?: Color.Unspecified
            )
        }
    }
}
