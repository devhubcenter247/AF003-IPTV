package com.dongbv.alf_03_iptv.style

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.dongbv.alf_03_iptv.R

// ── Font families ─────────────────────────────────────────────────────────────
// Project ships rb_* (Roboto) weights; mapped to Inter/Montserrat roles per spec
val RbFontFamily = FontFamily(
    Font(R.font.rb_400, FontWeight.Normal),
    Font(R.font.rb_500, FontWeight.Medium),
    Font(R.font.rb_600, FontWeight.SemiBold),
    Font(R.font.rb_700, FontWeight.Bold),
    Font(R.font.rb_800, FontWeight.ExtraBold),
)

// ── IPTV Typography ───────────────────────────────────────────────────────────
val IptvTypography = Typography(
    // App Title / Logo — 24sp Bold, letter-spacing 1.5
    displayLarge = TextStyle(
        fontFamily = RbFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 1.5.sp,
    ),
    // Section Heading — 20sp SemiBold
    headlineMedium = TextStyle(
        fontFamily = RbFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.sp,
    ),
    // Card Title — 16sp Medium
    titleMedium = TextStyle(
        fontFamily = RbFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
    ),
    // Body — 14sp Regular
    bodyMedium = TextStyle(
        fontFamily = RbFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    ),
    // Caption — 12sp Regular
    bodySmall = TextStyle(
        fontFamily = RbFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
    ),
    // Button — 14sp SemiBold
    labelLarge = TextStyle(
        fontFamily = RbFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp,
    ),
    // Tab / Nav Label — 11sp Medium
    labelSmall = TextStyle(
        fontFamily = RbFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp,
    ),
)

// ── Material3 color schemes ───────────────────────────────────────────────────
private val IptvDarkColorScheme = darkColorScheme(
    primary = IptvAccent,
    onPrimary = IptvTextPrimary,
    secondary = IptvAccentSecondary,
    onSecondary = IptvTextPrimary,
    background = IptvBgPrimary,
    onBackground = IptvTextPrimary,
    surface = IptvSurface,
    onSurface = IptvTextPrimary,
    surfaceVariant = IptvBgSecondary,
    onSurfaceVariant = IptvTextSecondary,
    outline = IptvDivider,
    error = IptvError,
    onError = IptvTextPrimary,
)

private val IptvLightColorScheme = lightColorScheme(
    primary = IptvAccent,
    onPrimary = IptvTextPrimary,
    secondary = IptvAccentSecondary,
    onSecondary = IptvTextPrimary,
    background = IptvLightBgPrimary,
    onBackground = IptvLightTextPrimary,
    surface = IptvLightSurface,
    onSurface = IptvLightTextPrimary,
    surfaceVariant = IptvLightBgSecondary,
    onSurfaceVariant = IptvLightTextSecondary,
    outline = Color(0xFFDDE1EC),
    error = IptvError,
    onError = IptvTextPrimary,
)

// ── Extended IPTV color tokens (accessed via LocalIptvColors) ─────────────────
@Stable
data class IptvColors(
    val bgPrimary: Color,
    val bgSecondary: Color,
    val surface: Color,
    val accent: Color,
    val accentSecondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val divider: Color,
    val isDark: Boolean,
)

private val DarkIptvColors = IptvColors(
    bgPrimary = IptvBgPrimary,
    bgSecondary = IptvBgSecondary,
    surface = IptvSurface,
    accent = IptvAccent,
    accentSecondary = IptvAccentSecondary,
    textPrimary = IptvTextPrimary,
    textSecondary = IptvTextSecondary,
    textTertiary = IptvTextTertiary,
    success = IptvSuccess,
    warning = IptvWarning,
    error = IptvError,
    divider = IptvDivider,
    isDark = true,
)

private val LightIptvColors = IptvColors(
    bgPrimary = IptvLightBgPrimary,
    bgSecondary = IptvLightBgSecondary,
    surface = IptvLightSurface,
    accent = IptvAccent,
    accentSecondary = IptvAccentSecondary,
    textPrimary = IptvLightTextPrimary,
    textSecondary = IptvLightTextSecondary,
    textTertiary = IptvTextTertiary,
    success = IptvSuccess,
    warning = IptvWarning,
    error = IptvError,
    divider = Color(0xFFDDE1EC),
    isDark = false,
)

val LocalIptvColors = compositionLocalOf { DarkIptvColors }

/** Shortcut to access IPTV brand colors inside any @Composable */
val iptvColors: IptvColors
    @Composable get() = LocalIptvColors.current

// ── Context helpers ───────────────────────────────────────────────────────────
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

fun Context.getActivity(): Activity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun isDarkMode(context: Context): Boolean {
    val currentNightMode =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

// ── Root theme composable ─────────────────────────────────────────────────────
@Composable
fun AppViewTheme(
    lightStatusBar: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val dark = isDarkMode(context)
    val colorScheme = if (dark) IptvDarkColorScheme else IptvLightColorScheme
    val iptvColors = if (dark) DarkIptvColors else LightIptvColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context.findActivity())?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, view)
            SideEffect {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                insetsController.apply {
                    hide(WindowInsetsCompat.Type.systemBars())
                    hide(WindowInsetsCompat.Type.statusBars())
                    hide(WindowInsetsCompat.Type.navigationBars())
                    systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    isAppearanceLightStatusBars = lightStatusBar
                }
            }
        }
    }

    CompositionLocalProvider(LocalIptvColors provides iptvColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = IptvTypography,
            content = content,
        )
    }
}
