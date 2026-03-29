package com.dongbv.alf_03_iptv.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.app.ProjectApplication
import com.dongbv.alf_03_iptv.app.PreferenceData
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style._8Sdp
import java.util.Locale

@Stable
data class InnerPadding(
    val top: Dp = 0.dp,
    val left: Dp = 0.dp,
    val right: Dp = 0.dp,
    val bottom: Dp = 0.dp,
    val keyboardHeight: Dp = 0.dp
) {
    companion object {
        val Zero = InnerPadding()
        fun all(value: Dp) = InnerPadding(value, value, value, value, value)
        fun horizontal(value: Dp) = InnerPadding(left = value, right = value)
        fun vertical(value: Dp) = InnerPadding(top = value, bottom = value)
        fun top(value: Dp) = InnerPadding(top = value)
        fun bottom(value: Dp) = InnerPadding(bottom = value)
        fun left(value: Dp) = InnerPadding(left = value)
        fun right(value: Dp) = InnerPadding(right = value)
        fun keyboard(value: Dp) = InnerPadding(keyboardHeight = value)
    }
}

abstract class BaseActivity(private val lightStatus: Boolean = true) : AppCompatActivity() {
    abstract fun initialize()

    @Composable
    abstract fun BoxScope.ContentView(innerPadding: InnerPadding, context: Context)
    open fun isFinishFirstFlow(): Boolean = true

    @Composable
    open fun ViewDialog() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialize()
        setContent { ComposeContent() }
    }

    @Composable
    private fun ComposeContent() {
        val focusManager = LocalFocusManager.current
        val layoutDirection = LocalLayoutDirection.current
        val density = LocalDensity.current
        val sysBars = WindowInsets.safeDrawing.asPaddingValues()
        val ime = WindowInsets.ime
        val imeBottom = ime.getBottom(density)
        val imeVisible = imeBottom > 0
        val imeBottomDp by animateDpAsState(
            targetValue = with(density) { imeBottom.toDp() },
            label = "ime-bottom-anim"
        )

        val topPadding = sysBars.calculateTopPadding() + _8Sdp
        val leftPadding = sysBars.calculateLeftPadding(layoutDirection)
        val rightPadding = sysBars.calculateRightPadding(layoutDirection)
        val bottomPadding = maxOf(sysBars.calculateBottomPadding(), 0.dp)
        val keyboardPadding = if (imeVisible) imeBottomDp else 0.dp

        val calculatedPadding = remember(
            topPadding, leftPadding, rightPadding, bottomPadding, keyboardPadding
        ) {
            InnerPadding(
                top = topPadding,
                left = leftPadding,
                right = rightPadding,
                bottom = bottomPadding,
                keyboardHeight = keyboardPadding
            )
        }

        AppViewTheme(lightStatus) {

            Box(
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    }
            ) {
                ContentView(calculatedPadding, applicationContext)
            }
            ViewDialog()
        }
    }

    private var activityResultCallback: ((ActivityResult) -> Unit)? = null

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        listenerResult(result)
        activityResultCallback?.invoke(result)
        activityResultCallback = null
    }

    fun launchForResult(intent: Intent, callback: (ActivityResult) -> Unit) {
        activityResultCallback = callback
        resultLauncher.launch(intent)
    }

    open fun listenerResult(result: ActivityResult) {}

    inline fun <reified T : Any> launchActivity(vararg params: Pair<String, Any?>) {
        startActivity(createIntent<T>(*params))
    }

    inline fun <reified T : Any> launchAndFinish(vararg params: Pair<String, Any?>) {
        launchActivity<T>(*params)
        finish()
    }

    inline fun <reified T : Any> launchAndClearTask(vararg params: Pair<String, Any?>) {
        startActivity(createIntent<T>(*params).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    inline fun <reified T : Any> createIntent(vararg params: Pair<String, Any?>): Intent {
        return Intent(this, T::class.java).apply {
            params.forEach { (key, value) ->
                runCatching { putExtraSmart(key, value) }
                    .onFailure { e ->
                        android.util.Log.w(TAG, "Skip extra \"$key\": ${e.message}")
                    }
            }
        }
    }

    fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { startActivity(it) }
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(null)
            return
        }
        val languageCode = if (isFinishFirstFlow()) {
            PreferenceData.languageCode
        } else {
            ProjectApplication.languageCodeDefault
        }
        val localizedContext = applyLocale(newBase, languageCode)
        super.attachBaseContext(localizedContext)
    }

    fun applyLocale(context: Context, languageCode: String?): Context {
        val locale = createLocale(languageCode ?: ProjectApplication.languageCodeDefault)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }

    private fun createLocale(languageCode: String): Locale {
        val parts = languageCode.split("_")
        return if (parts.size > 1) {
            val region = parts[1].removePrefix("r")
            Locale.Builder()
                .setLanguage(parts[0])
                .setRegion(region)
                .build()
        } else {
            Locale.Builder()
                .setLanguage(parts[0])
                .build()
        }
    }

    companion object {
        const val TAG = "BaseActivity"
    }
}
