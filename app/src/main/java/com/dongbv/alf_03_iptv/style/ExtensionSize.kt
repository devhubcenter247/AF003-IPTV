package com.dongbv.alf_03_iptv.style

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator
import kotlin.math.roundToInt
import kotlin.math.sqrt
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.Density
import com.sdp.ssp.android.Sdp

@Composable
fun Space(w: Int = 0, h: Int = 0) {
    Spacer(
        modifier = Modifier
            .width(w.dp)
            .height(h.dp)
    )
}
@Composable
fun SpaceH(h: Int) {
    Spacer(
        modifier = Modifier
            .height(h.dp)
    )
}
@Composable
fun SpaceW(w: Int) {
    Spacer(
        modifier = Modifier
            .width(w.dp)
    )
}
@Composable
fun ColumnScope.SpaceF(weight: Float = 1f) {
    Spacer(Modifier.weight(weight))
}
@Composable
fun RowScope.SpaceF(weight: Float = 1f) {
    Spacer(Modifier.weight(weight))
}
@Composable
fun Space(w: Dp = 0.dp, h: Dp =0.dp) {
    Spacer(
        modifier = Modifier
            .width(w)
            .height(h)
    )
}
@Composable
fun SpaceH(h: Dp) {
    Spacer(
        modifier = Modifier
            .height(h)
    )
}
@Composable
fun SpaceW(w: Dp) {
    Spacer(
        modifier = Modifier
            .width(w)
    )
}
@Composable
@SuppressLint("ConfigurationScreenWidthHeight")
@Stable
fun getScreenWidthInDp(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.dp
}
@Composable
@SuppressLint("ConfigurationScreenWidthHeight")
@Stable
fun getScreenHeightInDp(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp.dp
}
@Stable
fun getScreenSizeInInches(context: Context): Double {
    val displayMetrics = context.resources.displayMetrics
    val widthInPixels = displayMetrics.widthPixels
    val heightInPixels = displayMetrics.heightPixels
    val widthInDp = widthInPixels / displayMetrics.density
    val heightInDp = heightInPixels / displayMetrics.density
    val screenWidthInInches = widthInDp / displayMetrics.densityDpi
    val screenHeightInInches = heightInDp / displayMetrics.densityDpi
    return sqrt(
        (screenWidthInInches * screenWidthInInches) +
                (screenHeightInInches * screenHeightInInches).toDouble()
    )
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Modifier.systemBarPadding(
    insets: WindowInsets,
    side: WindowInsetsSides,
    previewPadding: Modifier.() -> Modifier,
): Modifier =
    if (LocalInspectionMode.current) previewPadding()
    else windowInsetsPadding(insets.only(side))

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Modifier.statusBarPadding(): Modifier =
    systemBarPadding(
        insets = WindowInsets.statusBarsIgnoringVisibility,
        side = WindowInsetsSides.Top,
        previewPadding = { paddingTop(24.Sdp) },
    )

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Modifier.navigationBarPadding(): Modifier =
    systemBarPadding(
        insets = WindowInsets.navigationBarsIgnoringVisibility,
        side = WindowInsetsSides.Bottom,
        previewPadding = { paddingBottom(24.Sdp) },
    )
@Composable
@Stable
fun getScreenSize(): Size {
    val context = LocalContext.current

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = WindowMetricsCalculator.getOrCreate()
            .computeCurrentWindowMetrics(context)
        val width = windowMetrics.bounds.width()
        val height = windowMetrics.bounds.height()
        Size(width, height)
    } else {
        val displayMetrics = DisplayMetrics()
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        Size(width, height)
    }
}
@Composable
@Stable
fun getScreenWidth(): Int {
    return getScreenSize().width
}
@Composable
@Stable
fun getScreenHeight(): Int {
    return getScreenSize().height
}
@Composable
@Stable
fun getScreenWidthInPx(): Int {
    val metrics = LocalResources.current.displayMetrics
    return metrics.widthPixels
}
@Composable
@Stable
fun getScreenHeightInPx(): Int {
    val metrics = LocalResources.current.displayMetrics
    return metrics.heightPixels
}
@Composable
@Stable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}
@Composable
@Stable
fun Float.pxToDp(): Dp {
    return with(LocalDensity.current) { this@pxToDp.toDp() }
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight", "UnnecessaryComposedModifier")
fun Modifier.widthPercent(percent: Float): Modifier = composed {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    this.then(Modifier.width(screenWidth * (percent / 100f)))
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight", "UnnecessaryComposedModifier")
fun Modifier.heightPercent(percent: Float): Modifier = composed {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    this.then(Modifier.height(screenHeight * (percent / 100f)))
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight", "UnnecessaryComposedModifier")
fun Modifier.paddingHorizontal(values: Dp): Modifier = composed {
    this.then(Modifier.padding(horizontal = values))
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight", "UnnecessaryComposedModifier")
fun Modifier.paddingVertical(values: Dp): Modifier = composed {
    this.then(Modifier.padding(vertical = values))
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight", "UnnecessaryComposedModifier")
fun Modifier.paddingTop(values: Dp): Modifier = composed {
    this.then(Modifier.padding(top = values))
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight", "UnnecessaryComposedModifier")
fun Modifier.paddingBottom(values: Dp): Modifier = composed {
    this.then(Modifier.padding(bottom = values))
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight")
fun Modifier.paddingStart(values: Dp): Modifier = composed {
    this.then(Modifier.padding(start = values))
}
@Stable
@SuppressLint("ConfigurationScreenWidthHeight", "UnnecessaryComposedModifier")
fun Modifier.paddingEnd(values: Dp): Modifier = composed {
    this.then(Modifier.padding(end = values))
}
@Composable
@Stable
fun widthPercentPx(percent: Float): Int {
    val screenWidth = getScreenWidthInPx()
    return ((screenWidth * percent) / 100f).roundToInt()
}
@Composable
@Stable
fun heightPercentPx(percent: Float): Int {
    val screenHeight = getScreenHeightInPx()
    return ((screenHeight * percent) / 100f).roundToInt()
}
@Composable
@Stable
fun widthPercentDp(percent: Float): Dp {
    val screenWidthPx = getScreenWidthInPx()
    val widthPx = (screenWidthPx * percent / 100f)
    return with(LocalDensity.current) { widthPx.toDp() }
}
@Composable
@Stable
fun heightPercentDp(percent: Float): Dp {
    val screenHeightPx = getScreenHeightInPx()
    val heightPx = (screenHeightPx * percent / 100f)
    return with(LocalDensity.current) { heightPx.toDp() }
}
@Stable
fun Context.getScreenWidthInDp(): Dp {
    return resources.configuration.screenWidthDp.dp
}
@Stable
fun Context.getScreenWidthInPx(): Int {
    return resources.displayMetrics.widthPixels
}
@Stable
fun Context.getScreenHeightInDp(): Dp {
    return resources.configuration.screenHeightDp.dp
}
@Stable
fun Context.getScreenHeightInPx(): Int {
    return resources.displayMetrics.heightPixels
}
@Stable
fun Dp.toPx(density: Density): Float = with(density) { this@toPx.toPx() }



