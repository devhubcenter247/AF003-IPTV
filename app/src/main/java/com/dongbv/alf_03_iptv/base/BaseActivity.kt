package com.dongbv.alf_03_iptv.base

import com.dongbv.alf_03_iptv.analytics.AppScreen
import com.dongbv.alf_03_iptv.analytics.ScreenAnalytics
import com.dongbv.alf_03_iptv.ads.wrapper.interstitial.InterstitialAdConfig
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.ProjectApplication
import com.dongbv.alf_03_iptv.app.observeNetworkConnectivity
import com.dongbv.alf_03_iptv.app.openWifiSettings
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.white
import com.dongbv.alf_03_iptv.ui.component.DialogNoInternet
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.pxToDp
import java.util.Locale

abstract class BaseActivity : ComponentActivity() {

    abstract val analyticsScreen: AppScreen

    open val lightStatus: Boolean = true
    var isShowDialogLoading by mutableStateOf(false)
    var isShowDialogInternet by mutableStateOf(false)
    abstract fun initialize()

    @Composable
    abstract fun ContentView()
    open fun isFinishFirstFlow(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            ScreenAnalytics.track(this, analyticsScreen)
        }
        window.setFullScreen()
        window.hideSystemBar()
        enableEdgeToEdge()
        initialize()
        setContent { ComposeContent() }
        observeNetworkConnectivity(
            onNetworkAvailable = {
                isShowDialogInternet = false
            },
            onNetworkLost = { isShowDialogInternet = true }) {
            isShowDialogInternet = false
        }
    }

    @Composable
    private fun ComposeContent() {
        val focusManager = LocalFocusManager.current
        val density = LocalDensity.current
        val ime = WindowInsets.ime
        val imeBottom = ime.getBottom(density)
        val imeVisible = imeBottom > 0
        val keyboardState = remember(imeBottom, imeVisible) {
            KeyboardState(height = imeBottom, visible = imeVisible)
        }
        CompositionLocalProvider(LocalKeyboardState provides keyboardState) {

            AppViewTheme(lightStatus) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus(force = true) })
                        }
                ) {
                    ContentView()
                }
            }
            if (isShowDialogLoading) {
                Dialog(
                    onDismissRequest = {
                        isShowDialogLoading = false
                    },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = true,
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.Sdp),
                        color = Color.Black.copy(alpha = 0.75f),
                        modifier = Modifier.size(72.Sdp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.Sdp),
                                color = white,
                                strokeWidth = 4.dp,
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }
                }
            }

            DialogNoInternet(
                isShowDialog = isShowDialogInternet,
                onDismiss = { isShowDialogInternet = false },
                tryAgain = { openWifiSettings() },
            )
        }
    }

    private var navigationPending = false

    protected fun navigateWithInterstitial(action: () -> Unit) {
        if (navigationPending || InterstitialAdConfig.isShowingInterAll || isFinishing || isDestroyed) return
        navigationPending = true
        InterstitialAdConfig.showInterAll(this) {
            if (!isFinishing && !isDestroyed) action()
            window.decorView.post { navigationPending = false }
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
                        Log.w(TAG, "Skip extra \"$key\": ${e.message}")
                    }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(null)
            return
        }
        val languageCode = if (isFinishFirstFlow()) {
            AppPreferences.languageCode
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
