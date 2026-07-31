package com.dongbv.alf_03_iptv.ui.main.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.dongbv.alf_03_iptv.style.AppViewTheme
import com.dongbv.alf_03_iptv.style.ResponsivePreview
import com.dongbv.alf_03_iptv.ui.main.watch.WatchNowContent
import com.dongbv.alf_03_iptv.viewmodel.WatchNowViewModel
import org.koin.compose.viewmodel.koinViewModel

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
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
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

    WatchNowContent(
        channels = channels,
        isMuted = isMuted,
        pagePlayerMap = pagePlayerMap,
        autoNextEvent = viewModel.autoNextEvent,
        statusBarHeight = statusBarHeight,
        onBack = onBack,
        onToggleMute = { viewModel.toggleMute() },
        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
        onPageSelected = { page -> viewModel.updateWindow(page) }
    )
}

@UnstableApi
@ResponsivePreview
@Composable
private fun ChannelScreenPreview() {
    AppViewTheme {
        ChannelScreen()
    }
}
