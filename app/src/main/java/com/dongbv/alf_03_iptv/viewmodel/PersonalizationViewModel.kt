package com.dongbv.alf_03_iptv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dongbv.alf_03_iptv.data.local.entity.CategoryModel
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity
import com.dongbv.alf_03_iptv.data.local.entity.SourceType
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import com.dongbv.alf_03_iptv.utils.M3uParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonalizationViewModel(
    application: Application,
    private val repository: AppRepository
) : AndroidViewModel(application) {

    sealed class SaveState {
        data object Idle : SaveState()
        data object Loading : SaveState()
        data object Success : SaveState()
        data class Error(val message: String) : SaveState()
    }

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    fun saveSelectedCategories(selected: List<CategoryModel>) {
        if (selected.isEmpty()) return
        viewModelScope.launch {
            _saveState.value = SaveState.Loading
            try {
                val context = getApplication<Application>()
                withContext(Dispatchers.IO) {
                    selected
                        .filter { !repository.playlistExists(it.assetPath) }
                        .map { category ->
                            async {
                                val name = context.getString(category.title)
                                val channels = runCatching {
                                    M3uParser.parseFromAsset(context, category.assetPath)
                                }.getOrDefault(emptyList())
                                val playlist = PlaylistEntity(
                                    name = name,
                                    source = category.assetPath,
                                    sourceType = SourceType.FILE.value,
                                    channelCount = channels.size.takeIf { it > 0 } ?: category.count
                                )
                                repository.insertPlaylistWithChannels(playlist, channels)
                            }
                        }
                        .awaitAll()
                }
                _saveState.value = SaveState.Success
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
