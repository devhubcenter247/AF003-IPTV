package com.dongbv.alf_03_iptv.ui.main.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowActions
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowContent
import com.dongbv.alf_03_iptv.viewmodel.WatchNowViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.unit.dp
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.style.backgroundResource

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun ChannelScreen(
    @Suppress("UNUSED_PARAMETER") modifier: Modifier = Modifier,
    viewModel: WatchNowViewModel = koinViewModel(),
    onBack: () -> Unit = {}
) {
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val isMuted by viewModel.isMuted.collectAsStateWithLifecycle()
    val pagePlayerMap by viewModel.pagePlayerMap.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.resumePlayback()
                Lifecycle.Event.ON_PAUSE -> viewModel.pausePlayback()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.pausePlayback()
        }
    }

    val actions = remember(viewModel, onBack) {
        object : WatchNowActions {
            override fun onBack() {
                onBack()
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
        isShowBack = false,
        pagePlayerMap = pagePlayerMap,
        autoNextEvent = viewModel.autoNextEvent,
        actions = actions
    )
}

@UnstableApi
@ResponsivePreview
@Composable
private fun ChannelScreenPreview() {
    AppViewTheme {
        WatchNowContent(
            channels = ChannelEntity.fakeList(),
            isMuted = false,
            modifier = Modifier.backgroundResource(R.drawable.img_bg_app),
            pagePlayerMap = emptyMap(),
            actions = WatchNowActions.None
        )
    }
}
