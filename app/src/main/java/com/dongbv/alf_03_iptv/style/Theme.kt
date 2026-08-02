package com.dongbv.alf_03_iptv.style

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

class Toaster(private val context: Context) {
    fun show(@StringRes res: Int, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, res, length).show()

    fun show(@StringRes res: Int, vararg args: Any) =
        Toast.makeText(context, context.getString(res, *args), Toast.LENGTH_SHORT).show()
    fun show(message:String, vararg args: Any) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun rememberToaster(): Toaster {
    val context = LocalContext.current
    return remember(context) { Toaster(context) }
}

@Composable
fun AppViewTheme(
    lightStatusBar: Boolean = false,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = remember(view) { view.context.findActivity()?.window }
        if (window != null) {
            val insetsController = remember(window, view) {
                WindowCompat.getInsetsController(window, view)
            }
            LaunchedEffect(window, view) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            LaunchedEffect(lightStatusBar) {
                insetsController.isAppearanceLightStatusBars = lightStatusBar
            }
        }
    }
    MaterialTheme(content = content)
}
