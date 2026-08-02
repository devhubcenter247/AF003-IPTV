package com.dongbv.alf_03_iptv.ui.main.watch

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.backgroundResource
import com.dongbv.alf_03_iptv.viewmodel.WatchNowViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("UnsafeOptInUsageError")
class WatchNowActivity : BaseActivity() {

    private val viewModel: WatchNowViewModel by viewModel()

    override fun initialize() {}

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
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
            statusBarHeight = statusBarHeight,
            actions = actions,
        )
    }
}
