package com.dongbv.alf_03_iptv.analytics

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.firebase.analytics.FirebaseAnalytics

object ScreenAnalytics {
    fun track(context: Context, screen: AppScreen) {
        FirebaseAnalytics.getInstance(context.applicationContext).logEvent(
            screen.eventName,
            Bundle().apply { putString("screen_name", screen.eventName.removePrefix("screen_")) }
        )
    }
}

/** One event on entry or a changed screen; ordinary recomposition sends nothing. */
@Composable
fun TrackScreen(screen: AppScreen) {
    if (LocalInspectionMode.current) return
    val context = LocalContext.current
    LaunchedEffect(screen) {
        ScreenAnalytics.track(context, screen)
    }
}
