package com.dongbv.alf_03_iptv.ads.wrapper.interstitial

import android.app.Activity
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.dongbv.alf_03_iptv.ads.model.AdInterConfig
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.interstitial.InterstitialAdConfig as SdkInterstitialConfig
import com.lib.ads.gma.ads.helper.interstitial.InterstitialAdHelper
import com.lib.ads.gma.ads.model.wrapper.ApAdError
import com.lib.ads.gma.ads.model.wrapper.ApInterstitialAd
import com.lib.ads.gma.ads.model.wrapper.InterstitialAdListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Each helper belongs to its Activity; only the SDK's tagged preload cache is shared. */
object InterstitialAdConfig {
    private const val PRELOAD_TAG = "inter_all"
    private val helpers = mutableMapOf<ComponentActivity, InterstitialAdHelper>()
    private var activeActivity: ComponentActivity? = null
    private var lastImpressionElapsed: Long? = null
    private var pendingAfterPlayer = false
    private val closed = MutableStateFlow(true)
    val isCloseInterAdManager = closed.asStateFlow()
    val isShowingInterAll: Boolean get() = activeActivity != null

    fun setCloseInterAdManager(isClose: Boolean) {
        closed.value = isClose
    }

    fun resetInterAllConfig() {
        lastImpressionElapsed = null
    }

    fun updateTimeShowInterAll() {
        lastImpressionElapsed = SystemClock.elapsedRealtime()
    }

    private fun canUseAds() = InterstitialPlacement.InterAll.canShowAds &&
        InterstitialPlacement.InterAll.listId.isNotEmpty() &&
        !AppPurchase.getInstance().isPurchased()

    private fun helper(activity: ComponentActivity): InterstitialAdHelper = helpers.getOrPut(activity) {
        val config = SdkInterstitialConfig.waterfall(
            adUnitIds = InterstitialPlacement.InterAll.listId,
            intervalBetweenAds = 0,
            autoReloadAfterShow = true,
            loadTimeoutMs = 30_000L,
            canShowAds = InterstitialPlacement.InterAll.canShowAds,
        ).setPreloadTag(PRELOAD_TAG)
        InterstitialAdHelper(activity, activity, config).also { helper ->
            activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    helpers.remove(activity)
                    helper.cancel()
                    helper.unregisterAllAdListeners()
                    if (activeActivity === activity) {
                        activeActivity = null
                        closed.value = true
                    }
                    owner.lifecycle.removeObserver(this)
                }
            })
        }
    }

    fun loadInterAll(activity: ComponentActivity) {
        if (canUseAds() && !activity.isFinishing && !activity.isDestroyed) {
            val helper = helper(activity)
            if (!helper.isAdLoaded()) helper.preload()
        }
    }

    fun recordPlayerClosed() {
        if (!AppPurchase.getInstance().isPurchased() && AppPreferences.registerPlayerClose()) {
            pendingAfterPlayer = true
        }
    }

    fun consumePendingAfterPlayer(): Boolean {
        if (!pendingAfterPlayer) return false
        pendingAfterPlayer = false
        return true
    }

    fun showInterAll(activity: Activity?, onNextAction: () -> Unit) {
        val owner = activity as? ComponentActivity
        val lastImpression = lastImpressionElapsed
        val intervalPassed = lastImpression == null ||
            SystemClock.elapsedRealtime() - lastImpression >= InterstitialPlacement.InterAll.timeInterval
        if (owner == null || owner.isFinishing || owner.isDestroyed ||
            !canUseAds() || !intervalPassed || isShowingInterAll
        ) {
            onNextAction()
            return
        }

        activeActivity = owner
        closed.value = false
        val helper = helper(owner)
        var continued = false
        fun markClosed() {
            if (activeActivity === owner) {
                activeActivity = null
                closed.value = true
            }
        }
        helper.waitLoadAndShow(
            activity = owner,
            timeoutMs = 15_000L,
            showWhenReturnFromBackground = true,
            callback = object : InterstitialAdListener {
                override fun onNextAction() {
                    if (!helper.isAdLoaded() || owner.isFinishing || owner.isDestroyed) markClosed()
                    if (continued) return
                    continued = true
                    onNextAction()
                }

                override fun onImpression(ad: ApInterstitialAd) {
                    updateTimeShowInterAll()
                }

                override fun onDismissed(ad: ApInterstitialAd) {
                    markClosed()
                }

                override fun onFailedToShow(error: ApAdError) {
                    markClosed()
                }
            }
        )
    }
}

sealed class InterstitialPlacement {
    abstract val interAdConfig: AdInterConfig
    val listId: List<String>
        get() = interAdConfig.listAds
            .filter { it.enableAd && it.adUnit.isNotBlank() }
            .map { it.adUnit }
    val canShowAds: Boolean get() = remoteConfig.adEnable && interAdConfig.enable
    val timeInterval: Long get() = interAdConfig.timeInterval.coerceAtLeast(0)

    data object InterAll : InterstitialPlacement() {
        override val interAdConfig: AdInterConfig get() = remoteConfig.interAllConfig
    }
}
