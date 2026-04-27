package com.dongbv.alf_03_iptv.style

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

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

@Composable
fun AppViewTheme(
    lightStatusBar: Boolean = false,
    content: @Composable () -> Unit,
) {

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
    content()
}
