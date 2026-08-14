package com.dongbv.alf_03_iptv.ui.main.add

import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.dongbv.alf_03_iptv.base.BaseActivity
import com.dongbv.alf_03_iptv.data.local.entity.MediaModel
import com.dongbv.alf_03_iptv.viewmodel.AddItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddChannelActivity : BaseActivity (){

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

    override fun initialize() {}

    @Composable
    override fun ContentView(statusBarHeight: Dp) {
        AddSChanelcreen(
            viewModel = viewModel,
            statusBarHeight = statusBarHeight,
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
            onPickGallery = {
                galleryPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                )
            },
            onShowLoading = { show -> isShowDialogLoading = show }
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
