package com.dongbv.alf_03_iptv.ui.main.watch

import com.dongbv.alf_03_iptv.analytics.AppScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.viewmodel.WatchNowViewModel
import com.lib.ads.gma.ads.billing.AppPurchase
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdHolderConfig
import com.lib.ads.gma.ads.helper.adnative.preload.NativeAdPreloadHolderOptions
import com.lib.ads.gma.ads.helper.adnative.preload.createNativeAdHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("UnsafeOptInUsageError")
class WatchNowActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.WATCH_NOW


    private val nativeFullAdHolder: NativeAdHolderConfig by createNativeAdHolder(
        enabled = !AppPurchase.getInstance().isPurchased() && NativePlacement.NativePlayList.canShowAds,
        scope = lifecycleScope,
        tag = "watch_now_full_native",
        options = NativeAdPreloadHolderOptions(
            fallbackAdUnitIds = NativePlacement.NativePlayList.listId,
            lifecycleOwner = this@WatchNowActivity,
            autoReloadOnResume = true,
            autoRequestOnStart = false,
        )
    )

    private val viewModel: WatchNowViewModel by viewModel()

    override fun initialize() {
        if (!AppPurchase.getInstance().isPurchased() && NativePlacement.NativePlayList.canShowAds) {
            nativeFullAdHolder.request()
        }
    }

    @Composable
    override fun ContentView() {
        val channels by viewModel.channels.collectAsStateWithLifecycle()
        val isMuted by viewModel.isMuted.collectAsStateWithLifecycle()
        val pagePlayerMap by viewModel.pagePlayerMap.collectAsStateWithLifecycle()

        val actions = remember(viewModel) {
            object : WatchNowActions {
                override fun onBack() {
                    finish()
                }

                override fun onToggleMute() {
                    viewModel.toggleMute()
                }

                override fun onToggleFavorite(channelId: Long) {
                    viewModel.toggleFavorite(channelId)
                }

                override fun onPageSelected(page: Int) {
                    viewModel.updateWindow(page)
                }
            }
        }

        WatchNowContent(
            channels = channels,
            isMuted = isMuted,
            modifier = Modifier.fillMaxSize().backgroundResource(R.drawable.img_bg_app),
            pagePlayerMap = pagePlayerMap,
            autoNextEvent = viewModel.autoNextEvent,
            actions = actions,
            nativeFullAdHolder = nativeFullAdHolder,
        )
    }
}
