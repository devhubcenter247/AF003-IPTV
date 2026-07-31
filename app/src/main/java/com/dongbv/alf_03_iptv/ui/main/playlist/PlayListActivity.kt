package com.dongbv.alf_03_iptv.ui.main.playlist

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.ui.main.play.PlayChannelActivity

class PlayListActivity : BaseActivity() {

    private var playlistId: Long = 0

    companion object {
        private const val EXTRA_PLAYLIST_ID = "extra_playlist_id"

        fun newIntent(context: Context, playlistId: Long): Intent =
            Intent(context, PlayListActivity::class.java).apply {
                putExtra(EXTRA_PLAYLIST_ID, playlistId)
            }
    }

    override fun initialize() {
        playlistId = intent.getLongExtra(EXTRA_PLAYLIST_ID, 0)
    }

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        PlayListScreen(
            playlistId = playlistId,
            onBack = { finish() },
            onOpenChannel = { channel ->
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
        )
    }
}
