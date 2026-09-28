package com.dongbv.alf_03_iptv.ui.main.playlist

import com.dongbv.alf_03_iptv.analytics.AppScreen
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dongbv.alf_03_iptv.ads.wrapper.native.NativePlacement
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.style.statusBarPadding
import com.dongbv.alf_03_iptv.ui.component.RatingDialog
import com.dongbv.alf_03_iptv.ui.main.play.PlayChannelActivity

class PlayListActivity : BaseActivity() {
    override val analyticsScreen = AppScreen.PLAYLIST

    private var playlistId: Long = 0
    private var isShowRating by mutableStateOf(false)

    companion object {
        private const val EXTRA_PLAYLIST_ID = "extra_playlist_id"

        fun newIntent(context: Context, playlistId: Long): Intent =
            Intent(context, PlayListActivity::class.java).apply {
                putExtra(EXTRA_PLAYLIST_ID, playlistId)
            }
    }

    override fun initialize() {
        playlistId = intent.getLongExtra(EXTRA_PLAYLIST_ID, 0)
        isShowRating = AppPreferences.consumeRateAfterPlaylistImport()
    }

    @Composable
    override fun ContentView() {
        PlayListScreen(
            playlistId = playlistId,
            nativePlacement = NativePlacement.NativeMediumAll,
            modifier = Modifier.statusBarPadding(),
            onBack = { finish() },
            onOpenChannel = { channel ->
                navigateWithInterstitial {
                    startActivity(
                        PlayChannelActivity.newIntent(
                            this@PlayListActivity,
                            channel.id,
                            channel.name,
                            channel.url,
                            channel.logo,
                            channel.playlistId
                        )
                    )
                }
            }
        )
        RatingDialog(
            isShowRate = isShowRating,
            onDismiss = { isShowRating = false },
            rateApp = {
                isShowRating = false
                AppPreferences.isRatingApp.value = true
            },
            sendEmailFeedback = {
                isShowRating = false
            }
        )
    }
}
