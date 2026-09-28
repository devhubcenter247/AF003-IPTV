package com.dongbv.alf_03_iptv.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.dongbv.alf_03_iptv.ui.personalization.PersonalizationActivity
import com.dongbv.alf_03_iptv.ui.splash.SplashActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@SuppressLint("StaticFieldLeak")
object AdActivityKillManager {
    private const val TAG = "AdActivityKillManager"

    private val AD_ACTIVITY_NAMES = setOf(
        // Google
        "AdActivity",
        // TikTok/Pangle
        "TTAppOpenAdActivity",
        "TTInterstitialActivity",
        "TTInterstitialExpressActivity",
        "TTFullScreenExpressVideoActivity",
        "TTFullScreenVideoActivity",
        "TTAdActivity",
        // Facebook
        "AudienceNetworkActivity",
        // Mintegral / MBridge
        "MBCommonActivity",
        "MBInterstitialVideoActivity",
        // Vungle / IronSource / ... nếu có thì add tiếp
    )

    // Những màn hình "nguồn" vừa chuyển sang ads (vd splash)
    // Must be simple-name Strings — compared against activity::class.java.simpleName below.
    // Mixing in raw Class<*> objects here would silently never match (Class != String).
    private val SOURCE_ACTIVITY_NAMES = setOf(
        SplashActivity::class.java.simpleName,
        PersonalizationActivity::class.java.simpleName,
    )

    private var currentAdActivity: Activity? = null

    private var isFromSource = false
    private var jobActivityClose: Job? = null

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                val activityName = activity::class.java.simpleName
                Log.d(TAG, "onResumed: $activityName (isFromSource=$isFromSource)")

                if (activityName in SOURCE_ACTIVITY_NAMES) {
                    isFromSource = true
                    Log.d(TAG, "onResumed: $activityName is a source screen -> isFromSource=true")
                }

                if (activityName in AD_ACTIVITY_NAMES && isFromSource) {
                    Log.d(TAG, "onResumed: $activityName is an ad activity right after a source screen -> arm watchdog")
                    currentAdActivity = activity
                    scheduleKillAd()
                    isFromSource = false
                } else if (activityName in AD_ACTIVITY_NAMES) {
                    Log.d(TAG, "onResumed: $activityName is an ad activity but isFromSource=false -> not armed")
                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (activity::class.java.simpleName in AD_ACTIVITY_NAMES) {
                    Log.d(TAG, "onPaused: ${activity::class.java.simpleName} -> cancel watchdog (ad closed normally)")
                    cancelKillAd()
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity::class.java.simpleName in AD_ACTIVITY_NAMES) {
                    Log.d(TAG, "onDestroyed: ${activity::class.java.simpleName} -> cancel watchdog")
                    cancelKillAd()
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
        })
    }

    private fun scheduleKillAd() {
        val config = remoteConfig.skipInterConfig
        Log.d(TAG, "scheduleKillAd: config enable=${config.enable} timeout_ms=${config.timeOutMS}")
        if (!config.enable || config.timeOutMS <= 0) {
            Log.d(TAG, "scheduleKillAd: abort -- disabled by config")
            return
        }
        val activity = currentAdActivity
        if (activity == null) {
            Log.d(TAG, "scheduleKillAd: abort -- currentAdActivity is null")
            return
        }
        if (activity::class.java.simpleName !in AD_ACTIVITY_NAMES) {
            Log.d(TAG, "scheduleKillAd: abort -- ${activity::class.java.simpleName} not in AD_ACTIVITY_NAMES")
            return
        }

        Log.d(TAG, "scheduleKillAd: watchdog armed for ${activity::class.java.simpleName}, will finish() in ${config.timeOutMS}ms unless closed first")
        jobActivityClose?.cancel()
        jobActivityClose = appScope.launch {
            delay(config.timeOutMS.milliseconds)
            Log.d(TAG, "scheduleKillAd: timeout elapsed -> safeFinishAd()")
            safeFinishAd()
        }
    }

    private fun safeFinishAd() {
        currentAdActivity?.let { activity ->
            if (!activity.isFinishing && !activity.isDestroyed) {
                Log.d(TAG, "safeFinishAd: force-finishing stuck ${activity::class.java.simpleName}")
                activity.finish()
            } else {
                Log.d(TAG, "safeFinishAd: ${activity::class.java.simpleName} already finishing/destroyed, no-op")
            }
        }
        currentAdActivity = null
        isFromSource = false
    }

    private fun cancelKillAd() {
        if (jobActivityClose != null) Log.d(TAG, "cancelKillAd: watchdog cancelled")
        jobActivityClose?.cancel()
        jobActivityClose = null
        currentAdActivity = null
        isFromSource = false
    }
}