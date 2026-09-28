package com.dongbv.alf_03_iptv.ui.splash

import com.dongbv.alf_03_iptv.analytics.AppScreen
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.dongbv.alf_03_iptv.BuildConfig
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.AdPreloadManager
import com.dongbv.alf_03_iptv.ads.IAPPurchase
import com.dongbv.alf_03_iptv.ads.wrapper.banner.BannerPlacement
import com.dongbv.alf_03_iptv.ads.wrapper.interstitial.InterstitialAdConfig
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeAdColors
import com.dongbv.alf_03_iptv.ads.wrapper.native.view.NativeComposeView
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.BoxParentContent
import com.dongbv.alf_03_iptv.style.GradientLinearProgressIndicator
import com.dongbv.alf_03_iptv.style.SpaceH
import com.dongbv.alf_03_iptv.style.TextView
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.style.getScreenWidthInDp
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.style.toPx
import com.dongbv.alf_03_iptv.style.transparent
import com.dongbv.alf_03_iptv.style.widthPercent
import com.dongbv.alf_03_iptv.ui.component.neonState
import com.dongbv.alf_03_iptv.ui.language.LanguageActivity
import com.dongbv.alf_03_iptv.ui.language.LanguageScreenType
import com.dongbv.alf_03_iptv.ui.main.MainActivity
import com.dongbv.alf_03_iptv.widget.IptvAccent
import com.dongbv.alf_03_iptv.widget.colorSecondary
import com.dongbv.alf_03_iptv.widget.grdEnd
import com.dongbv.alf_03_iptv.widget.grdStart
import com.dongbv.alf_03_iptv.widget.mainColor
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.ConsentManager
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdPreloadHolderOptions
import com.lib.ads.gma.ads.helper.adnative.preload.createNativeAdHolder

import com.lib.ads.gma.ads.helper.banner.params.BannerSize
import com.lib.ads.gma.ads.manager.InterstitialAdManager
import com.lib.ads.gma.ads.model.wrapper.ApAdError
import com.lib.ads.gma.ads.model.wrapper.ApNativeAd
import com.lib.ads.gma.ads.model.wrapper.InterstitialAdListener
import com.lib.ads.gma.ads.model.wrapper.NativeAdListener
import com.sdp.ssp.android.Sdp
import com.sdp.ssp.android.Ssp
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.SPLASH

    private val consentManager by lazy {
        ConsentManager(this)
    }

    private var splashAdsJob: Job? = null
    private var interShowStarted = false
    var canShowAds by mutableStateOf(false)

    override fun initialize() {
        lifecycleScope.launch(Dispatchers.Main) {
            awaitAll(
                async { remoteConfig.setupRemoteConfig(BuildConfig.DEBUG) },
                async { requestUmp() }
            )
            if (consentManager.getCanRequestAd()) {
                Log.d("SplashRequest", "Launch Init Ads")
                AppPurchase.getInstance()
                    .initBilling(
                        application,
                        purchaseItemList = IAPPurchase.listProductData
                    ).setLogProductDetail(true).getBillingAndAwaitInitAds(5000, 5000) {
                        continueAfterBilling()
                    }
            } else {
                goNextPage()
            }

        }
    }

    private var billingResolved = false
    private fun continueAfterBilling() {
        if (billingResolved) return
        billingResolved = true

        if (AppPurchase.getInstance().isPurchased()) {
            goNextPage()
        } else {
            canShowAds = remoteConfig.adEnable
            initAds()
        }
    }

    suspend fun requestUmp(): Boolean {
        consentManager.requestUMP()
        return consentManager.getCanRequestAd()
    }

    private fun initAds() {
        if (!canShowAds) {
            NativeAdColors.shimmerHighlight = transparent
            NativeAdColors.colorBgNativeAds = transparent
            goNextPage()
            return
        }

        val nativeResolved = CompletableDeferred<Unit>()
        val interReady = CompletableDeferred<Boolean>()
        val nativeEnabled = NativePlacement.NativeSplash.canShowAds &&
            NativePlacement.NativeSplash.listId.any { it.isNotBlank() }
        nativeSplash = createNativeAdHolder(
            enabled = nativeEnabled,
            tag = this::class.java.simpleName,
            options = NativeAdPreloadHolderOptions(
                fallbackAdUnitIds = NativePlacement.NativeSplash.listId,
                lifecycleOwner = this@SplashActivity,
                autoReloadOnResume = false,
                autoRequestOnStart = false
            )
        ).value
        nativeSplash?.registerAdCallback(adCallback = object : NativeAdListener {
            override fun onImpression(ad: ApNativeAd) {
                nativeResolved.complete(Unit)
            }

            override fun onFailed(error: ApAdError) {
                nativeResolved.complete(Unit)
            }
        })
        if (!nativeEnabled || nativeSplash == null) nativeResolved.complete(Unit)

        if (!isFinishFirstFlow()) {
            preloadAdsNextScreen()
            if (nativeEnabled) nativeSplash?.request()
            splashAdsJob = lifecycleScope.launch {
                nativeResolved.await()
                lifecycle.currentStateFlow.first { it.isAtLeast(Lifecycle.State.RESUMED) }
                goNextPage()
            }
            return
        }

        splashAdsJob = lifecycleScope.launch {
            val nativeFinishedFirst = withTimeoutOrNull(
                remoteConfig.interSplashConfig.totalTimeout.coerceAtLeast(1).milliseconds
            ) {
                select {
                    nativeResolved.onAwait { true }
                    interReady.onAwait { false }
                }
            }
            val shouldShowInter = when (nativeFinishedFirst) {
                true -> {
                    val ready = withTimeoutOrNull(5_000.milliseconds) { interReady.await() } == true
                    if (ready) delay(1_000.milliseconds)
                    ready
                }
                false -> {
                    val ready = interReady.await()
                    withTimeoutOrNull(3_000.milliseconds) { nativeResolved.await() }
                    ready
                }
                null -> false
            }
            lifecycle.currentStateFlow.first { it.isAtLeast(Lifecycle.State.RESUMED) }
            if (shouldShowInter) showSplashInter() else goNextPage()
        }
        nativeSplash?.request()
        AdPreloadManager.preloadInterSplashConfig(
            remoteConfig.interSplashConfig,
            onInterReady = { interReady.complete(true) },
            onNextAction = { interReady.complete(false) }
        )
    }

    private fun showSplashInter() {
        if (navigationCompleted || interShowStarted || isFinishing || isDestroyed) return
        interShowStarted = true
        InterstitialAdManager.onShowSplash(
            this@SplashActivity,
            listener = object : InterstitialAdListener {
                override fun onImpression(ad: com.lib.ads.gma.ads.model.wrapper.ApInterstitialAd) {
                    InterstitialAdConfig.updateTimeShowInterAll()
                }

                override fun onFailedToShow(error: ApAdError) = goNextPage()

                override fun onNextAction() = goNextPage()
            }
        )
    }

    private fun preloadAdsNextScreen() {
        AdPreloadManager.preloadNativeAdConfig(NativePlacement.Language1)
        AdPreloadManager.preloadNativeAdConfig(NativePlacement.Language2)
    }

    var nativeSplash by mutableStateOf<NativeAdHolderConfig?>(null)


    override fun isFinishFirstFlow() = AppPreferences.isFinishFirstFlow
    private var navigationCompleted = false

    fun goNextPage() {
        if (navigationCompleted || isFinishing || isDestroyed) return
        navigationCompleted = true
        splashAdsJob?.cancel()
        if (!AppPreferences.isFinishFirstFlow) {
            LanguageActivity.startLanguage(this@SplashActivity, LanguageScreenType.Language1)
            finish()
        } else {
            launchActivity<MainActivity>()
            finish()
        }
    }

    @Composable
    override fun ContentView() {

        BoxParentContent(onBack = {}, modifier = Modifier.fillMaxSize()) {
            SplashScreen(
                modifier = Modifier
                    .statusBarPadding()
            ) {
                if (canShowAds) {
                    NativeComposeView(
                        nativePreloadConfig = (nativeSplash to NativePlacement.NativeSplash)
                    )
                }
            }
        }
    }


}

@Preview(name = "PIXEL_4_XL", device = Devices.PIXEL_4_XL, showBackground = true)
@Composable
fun SplashPreview() {
    AppViewTheme {
        SplashScreen()
    }
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNativeView: (@Composable () -> Unit) = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .backgroundResource(R.drawable.img_bg_app)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpaceH(h = getScreenWidthInDp() * 0.35f)

        Image(
            painter = painterResource(R.drawable.img_logo_splash),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(92.Sdp)
                .neonState(true, outerRadius = 4.Sdp, shape = RoundedCornerShape(12.Sdp))
                .clip(RoundedCornerShape(12.Sdp))
        )

        SpaceH(h = 12.Sdp)
        Text(
            text = stringResource(R.string.app_name),
            fontFamily = FontFamily(Font(R.font.ss_700)),
            color = IptvAccent,
            textAlign = TextAlign.Center,
            fontSize = 18.Ssp,
            letterSpacing = 1.5.sp
        )

        Spacer(Modifier.weight(1f))
        TextView(
            text = stringResource(R.string.this_action_may_be_constant_adverting),
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            textSize = 11.Ssp,
            color = colorSecondary,
            font = R.font.ss_400
        )
        GradientLinearProgressIndicator(
            colors = listOf(grdStart, grdEnd),
            modifier = Modifier
                .height(6.dp)
                .widthPercent(60f),
            strokeCap = StrokeCap.Round,
            trackColor = mainColor.copy(alpha = 0.2f)
        )
        SpaceH(h = 32.dp)
        onNativeView()
    }
}
