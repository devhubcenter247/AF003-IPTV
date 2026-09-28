package com.dongbv.alf_03_iptv.ads.wrapper.reward

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.dongbv.alf_03_iptv.app.isNetworkAvailable
import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.manager.RewardAdManager
import com.lib.ads.gma.ads.model.wrapper.ApAdError
import com.lib.ads.gma.ads.model.wrapper.ApRewardAd
import com.lib.ads.gma.ads.model.wrapper.ApRewardItem
import com.lib.ads.gma.ads.model.wrapper.RewardAdListener


object RewardAdConfig {
    private val rewardEdit = RewardAdsWrapper(RewardPlacement.RewardEdit)

    fun loadRewardChat(activity: Activity) {
        rewardEdit.preload(activity)
    }
    fun showRewardEdit(
        activity: Activity?,
        onRewarded: () -> Unit,
        onAdNotReady: () -> Unit = {},
    ) {
        activity?.let {
            rewardEdit.showAd(it, onNextAction = onRewarded, onAdNotReady = onAdNotReady)
        } ?: onAdNotReady()
    }
}


class RewardAdsWrapper(
    private val config: RewardPlacement,
) {
    private var isShowingAd = false
    private var isRequestAds = false
    private var rewardAd: ApRewardAd? = null

    private fun isAdsReady(): Boolean {
        return rewardAd?.isReady() == true
    }

    fun preload(activity: Context) {
        if (!isRequestAds
            && !isAdsReady()
            && activity.isNetworkAvailable()
            && config.canShowAds
            && !AppPurchase.getInstance().isPurchased()
        ) {
            isRequestAds = true
            RewardAdManager.loadRewardAdList(
                tag = "Reward_All",
                ids = config.listId,
                listener = object : RewardAdListener {
                    override fun onLoaded(ad: ApRewardAd) {
                        super.onLoaded(ad)
                        rewardAd = ad
                        isRequestAds = false
                    }

                    override fun onFailed(error: ApAdError) {
                        super.onFailed(error)
                        isRequestAds = false
                    }
                }
            )
        }
    }

    fun showAd(
        activity: Activity,
        onNextAction: () -> Unit = {},
        onAdNotReady: () -> Unit = {},
    ) {
        if (isShowingAd) return
        if (activity.isFinishing || activity.isDestroyed) {
            onAdNotReady()
            return
        }
        if (AppPurchase.getInstance().isPurchased() || !config.canShowAds) {
            onNextAction()
            return
        }
        val ad = rewardAd
        if (ad == null || !ad.isReady()) {
            preload(activity.applicationContext)
            onAdNotReady()
            return
        }
        isShowingAd = true
        rewardAd = null
        var rewardEarned = false
        var completed = false
        fun complete() {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Handler(Looper.getMainLooper()).post { complete() }
                return
            }
            if (completed) return
            completed = true
            isShowingAd = false
            ad.clean()
            preload(activity.applicationContext)
            if (!activity.isFinishing && !activity.isDestroyed) {
                if (rewardEarned) onNextAction() else onAdNotReady()
            }
        }
        RewardAdManager.showReward(
            activity = activity,
            ad = ad,
            onNextAction = {},
            object : RewardAdListener {
                override fun onRewarded(ad: ApRewardAd, item: ApRewardItem) {
                    rewardEarned = true
                }

                override fun onDismissed(ad: ApRewardAd) {
                    complete()
                }

                override fun onFailedToShow(error: ApAdError) {
                    complete()
                }

                override fun onFailed(error: ApAdError) {
                    complete()
                }
            }
        )
    }

}

sealed class RewardPlacement {
    abstract val listId: List<String>
    abstract val canShowAds: Boolean

    data object RewardEdit : RewardPlacement() {
        override val listId
            get() = remoteConfig.rewarEditConfig.listAds
                .filter { it.enableAd }
                .map { it.adUnit }
        override val canShowAds
            get() = remoteConfig.rewarEditConfig.enable
    }
}
