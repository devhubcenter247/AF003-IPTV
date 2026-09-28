package com.dongbv.alf_03_iptv.viewmodel

import com.dongbv.alf_03_iptv.app.remote.remoteConfig
import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.app.AppPreferences
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.local.entity.MediaModel
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity
import com.dongbv.alf_03_iptv.data.local.entity.SourceType
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import com.dongbv.alf_03_iptv.utils.M3uParser
import com.lib.ads.gma.ads.billing.AppPurchase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddItemViewModel(
    private val repository: AppRepository,
    private val providerPolicy: com.dongbv.alf_03_iptv.utils.BlockedProviderPolicy,
) : ViewModel() {

    private var pendingRetry: (() -> Unit)? = null

    fun cancelPendingImport() {
        pendingRetry = null
    }

    fun retryPendingImport() {
        val retry = pendingRetry
        pendingRetry = null
        retry?.invoke()
    }

    private val _galleryMedia = MutableStateFlow<List<MediaModel?>>(listOf(null))
    val galleryMedia: StateFlow<List<MediaModel?>> = _galleryMedia.asStateFlow()

    private val _saveState = MutableSharedFlow<SaveState>()
    val saveState = _saveState.asSharedFlow()

    sealed class SaveState {
        data object Loading : SaveState()
        data class Success(val playlistId: Long, val blockedCount: Int = 0) : SaveState()
        data class Error(@StringRes val messageResId: Int) : SaveState()
        data object RewardRequired : SaveState()
    }

    private fun canImport(rewardGranted: Boolean): Boolean =
        rewardGranted || AppPurchase.getInstance().isPurchased() || !remoteConfig.rewarEditConfig.enable

    private fun registerImport(source: String) {
        AppPreferences.registerImportedPlaylist(source.trim().lowercase())
    }

    fun addGalleryMedia(mediaList: List<MediaModel>) {
        val currentList = _galleryMedia.value.toMutableList()
       
        currentList.removeAll { it == null }
       
        currentList.addAll(mediaList)
       
        currentList.add(0, null)

        _galleryMedia.value = currentList
    }

    fun removeGalleryMedia(media: MediaModel) {
        val currentList = _galleryMedia.value.toMutableList()
        currentList.remove(media)
       
        currentList.removeAll { it == null }
        currentList.add(0, null)
        _galleryMedia.value = currentList
    }

    fun clearGalleryMedia() {
        _galleryMedia.value = listOf(null)
    }

    fun saveGalleryPlaylist(playlistName: String, rewardGranted: Boolean = false) {
        if (pendingRetry != null) return
        viewModelScope.launch {
            _saveState.emit(SaveState.Loading)

            val mediaList = _galleryMedia.value.filterNotNull()
            if (mediaList.isEmpty()) {
                _saveState.emit(SaveState.Error(R.string.no_videos_selected))
                return@launch
            }

            if (playlistName.isBlank()) {
                _saveState.emit(SaveState.Error(R.string.playlist_name_required))
                return@launch
            }

            try {
                val source = mediaList.joinToString("|") { it.path }.trim().lowercase()
                if (!canImport(rewardGranted)) {
                    pendingRetry = { saveGalleryPlaylist(playlistName, rewardGranted = true) }
                    _saveState.emit(SaveState.RewardRequired)
                    return@launch
                }

                val playlist = PlaylistEntity(
                    name = playlistName.trim(),
                    source = "gallery_${System.currentTimeMillis()}",
                    sourceType = SourceType.GALLERY.value,
                    channelCount = mediaList.size
                )

               
                val channels = mediaList.map { media ->
                    ChannelEntity(
                        playlistId = 0,
                        name = media.name,
                        url = media.path,
                        logo = media.path,
                        duration = media.duration,
                        groupName = "Gallery"
                    )
                }

               
                val playlistId = repository.insertPlaylistWithChannels(playlist, channels)
                registerImport(source)
                AppPreferences.requestRateAfterPlaylistImport()
                _saveState.emit(SaveState.Success(playlistId))

            } catch (e: Exception) {
                _saveState.emit(SaveState.Error(R.string.failed_to_save_playlist))
            }
        }
    }

   

    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    private val _selectedFileName = MutableStateFlow<String?>(null)
    val selectedFileName: StateFlow<String?> = _selectedFileName.asStateFlow()

    private val _selectedSuggestionUrl = MutableStateFlow<String?>(null)
    val selectedSuggestionUrl: StateFlow<String?> = _selectedSuggestionUrl.asStateFlow()

    fun setSelectedFile(uri: Uri?, fileName: String?) {
        if (uri != null && fileName?.lowercase()?.let { it.endsWith(".m3u") || it.endsWith(".m3u8") } != true) {
            viewModelScope.launch {
                _saveState.emit(SaveState.Error(R.string.invalid_file_type))
            }
            return
        }
        _selectedFileUri.value = uri
        _selectedFileName.value = fileName
        _selectedSuggestionUrl.value = null
    }

    fun setSelectedSuggestion(url: String?) {
        _selectedSuggestionUrl.value = url
        _selectedFileUri.value = null
        _selectedFileName.value = null
    }

    fun clearSelectedFile() {
        _selectedFileUri.value = null
        _selectedFileName.value = null
        _selectedSuggestionUrl.value = null
    }

    fun saveFilePlaylist(playlistName: String, context: Context, rewardGranted: Boolean = false) {
        if (pendingRetry != null) return
        viewModelScope.launch {
            _saveState.emit(SaveState.Loading)

            val fileUri = _selectedFileUri.value
            if (fileUri == null) {
                _saveState.emit(SaveState.Error(R.string.no_file_selected))
                return@launch
            }

            if (playlistName.isBlank()) {
                _saveState.emit(SaveState.Error(R.string.playlist_name_required))
                return@launch
            }

            try {
                if (!canImport(rewardGranted)) {
                    pendingRetry = { saveFilePlaylist(playlistName, context.applicationContext, rewardGranted = true) }
                    _saveState.emit(SaveState.RewardRequired)
                    return@launch
                }
                val parsedChannels = withContext(Dispatchers.IO) {
                    M3uParser.parseFromUri(context, fileUri)
                }

                val channels = parsedChannels.filterNot {
                    providerPolicy.isBlocked(it.name) || providerPolicy.isBlocked(it.url)
                }
                val blockedCount = parsedChannels.size - channels.size
                if (channels.isEmpty()) {
                    _saveState.emit(SaveState.Error(
                        if (blockedCount > 0) R.string.all_channels_blocked else R.string.no_channels_found
                    ))
                    return@launch
                }

               
                val playlist = PlaylistEntity(
                    name = playlistName.trim(),
                    source = fileUri.toString(),
                    sourceType = SourceType.FILE.value,
                    channelCount = channels.size
                )

               
                val playlistId = repository.insertPlaylistWithChannels(playlist, channels)
                registerImport(fileUri.toString())
                AppPreferences.requestRateAfterPlaylistImport()
                clearSelectedFile()
                _saveState.emit(SaveState.Success(playlistId, blockedCount))

            } catch (e: Exception) {
                _saveState.emit(SaveState.Error(R.string.failed_to_parse_file))
            }
        }
    }

    fun saveUrlPlaylist(playlistName: String, url: String, type: SourceType = SourceType.URL, rewardGranted: Boolean = false) {
        if (pendingRetry != null) return
        viewModelScope.launch {
            _saveState.emit(SaveState.Loading)

            if (playlistName.isBlank()) {
                _saveState.emit(SaveState.Error(R.string.playlist_name_required))
                return@launch
            }

            if (url.isBlank()) {
                _saveState.emit(SaveState.Error(R.string.url_required))
                return@launch
            }

            if (providerPolicy.isBlocked(url)) {
                _saveState.emit(SaveState.Error(R.string.provider_url_blocked))
                return@launch
            }

            try {
                if (!canImport(rewardGranted)) {
                    pendingRetry = { saveUrlPlaylist(playlistName, url, type, rewardGranted = true) }
                    _saveState.emit(SaveState.RewardRequired)
                    return@launch
                }
                val parsedChannels = withContext(Dispatchers.IO) {
                    M3uParser.parseFromUrl(url.trim(), providerPolicy::isBlocked)
                }

                val channels = parsedChannels.filterNot {
                    providerPolicy.isBlocked(it.name) || providerPolicy.isBlocked(it.url)
                }
                val blockedCount = parsedChannels.size - channels.size
                if (channels.isEmpty()) {
                    _saveState.emit(SaveState.Error(
                        if (blockedCount > 0) R.string.all_channels_blocked else R.string.no_channels_found
                    ))
                    return@launch
                }
                val playlist = PlaylistEntity(
                    name = playlistName.trim(),
                    source = url,
                    sourceType = type.value,
                    channelCount = channels.size
                )
                val playlistId = repository.insertPlaylistWithChannels(playlist, channels)
                registerImport(url)
                AppPreferences.requestRateAfterPlaylistImport()
                _saveState.emit(SaveState.Success(playlistId, blockedCount))

            } catch (e: com.dongbv.alf_03_iptv.utils.BlockedProviderUrlException) {
                _saveState.emit(SaveState.Error(R.string.provider_url_blocked))
            } catch (e: Exception) {
                _saveState.emit(SaveState.Error(R.string.failed_to_parse_url))
            }
        }
    }

}
