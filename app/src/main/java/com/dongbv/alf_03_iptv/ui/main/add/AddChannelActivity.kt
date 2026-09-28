package com.dongbv.alf_03_iptv.ui.main.add

import com.dongbv.alf_03_iptv.analytics.AppScreen
import android.widget.Toast
import com.dongbv.alf_03_iptv.R
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dongbv.alf_03_iptv.ads.wrapper.banner.BannerPlacement
import com.dongbv.alf_03_iptv.ads.wrapper.banner.rememberBannerPlacement
import com.dongbv.alf_03_iptv.ads.wrapper.reward.RewardAdConfig
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.data.local.entity.MediaModel
import com.dongbv.alf_03_iptv.viewmodel.AddItemViewModel
import com.lib.ads.gma.ads.helper.banner.params.BannerSize
import com.lib.ads.gma.ads.helper.banner.preload.BannerAdCard
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddChannelActivity : BaseActivity (){
    override val analyticsScreen = AppScreen.ADD_CHANNEL


    companion object {
        const val EXTRA_SAVED_PLAYLIST_ID = "extra_saved_playlist_id"
    }

    private val viewModel: AddItemViewModel by viewModel()

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {}
            viewModel.setSelectedFile(uri, getDisplayName(uri))
        }
    }

    private val galleryPickerLauncher = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        val mediaList = uris.mapNotNull { toMediaModel(it) }
        if (mediaList.isNotEmpty()) viewModel.addGalleryMedia(mediaList)
    }

    override fun initialize() {
        RewardAdConfig.loadRewardChat(this)
    }

    @Composable
    override fun ContentView() {
        AddSChanelScreen(
            viewModel = viewModel,
            onBack = ::finish,
            onPickFile = {
                filePickerLauncher.launch(
                    arrayOf(
                        "application/x-mpegurl",
                        "audio/x-mpegurl",
                        "application/vnd.apple.mpegurl",
                        "application/octet-stream"
                    )
                )
            },
            onGoggleAd = {
                if(!BannerPlacement.BannerAll.canShowAds) return@AddSChanelScreen
                val bannerConfig = rememberBannerPlacement(
                    BannerPlacement.BannerAll,
                    autoReloadOnResume = true,
                    bannerSize = BannerSize.LargePortraitAdaptive
                )

                BannerAdCard(
                    holder = bannerConfig,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            },
            onPickGallery = {
                galleryPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                )
            },
            onShowLoading = { show -> isShowDialogLoading = show },
            onWatchReward = {
                RewardAdConfig.showRewardEdit(
                    activity = this@AddChannelActivity,
                    onRewarded = { viewModel.retryPendingImport() },
                    onAdNotReady = {
                        viewModel.cancelPendingImport()
                        Toast.makeText(this@AddChannelActivity, R.string.reward_ad_not_ready, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onPlaylistSaved = { playlistId ->
                setResult(
                    RESULT_OK,
                    Intent().putExtra(EXTRA_SAVED_PLAYLIST_ID, playlistId)
                )
                finish()
            }
        )
    }

    private fun getDisplayName(uri: Uri): String? = try {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val col = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && col >= 0) cursor.getString(col) else null
        }
    } catch (_: Exception) {
        null
    }

    private fun toMediaModel(uri: Uri): MediaModel? = try {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameCol = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (!cursor.moveToFirst()) return null
            val name = if (nameCol >= 0) cursor.getString(nameCol) else "Video"
            val size = if (sizeCol >= 0) cursor.getLong(sizeCol) else 0L
            MediaModel(
                id = System.currentTimeMillis() + uri.hashCode(),
                name = name.substringBeforeLast('.'),
                duration = 0L,
                path = uri.toString(),
                size = size
            )
        }
    } catch (_: Exception) {
        null
    }
}
