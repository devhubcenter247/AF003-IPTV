package com.dongbv.alf_03_iptv.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dongbv.alf_03_iptv.data.local.entity.CategoryModel
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity
import com.dongbv.alf_03_iptv.data.local.entity.SourceType
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import com.dongbv.alf_03_iptv.utils.BlockedProviderPolicy
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
    private val repository: AppRepository,
    private val providerPolicy: BlockedProviderPolicy,
) : AndroidViewModel(application) {

    sealed class SaveState {
        data object Idle : SaveState()
        data object Loading : SaveState()
        data class Success(val blockedCount: Int = 0) : SaveState()
        data class Error(val message: String) : SaveState()
    }

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    fun saveSelectedCategories(context: Context, selected: List<CategoryModel>) {
        if (selected.isEmpty()) return
        viewModelScope.launch {
            _saveState.value = SaveState.Loading
            try {
                val blockedCount = withContext(Dispatchers.IO) {
                    selected
                        .filter { !repository.playlistExists(it.assetPath) }
                        .map { category ->
                            async {
                                val name = context.getString(category.title)
                                val parsedChannels = M3uParser.parseFromAsset(context, category.assetPath)
                                check(parsedChannels.isNotEmpty()) {
                                    "No channels found for ${category.assetPath}"
                                }
                                val channels = parsedChannels.filterNot {
                                    providerPolicy.isBlocked(it.name) || providerPolicy.isBlocked(it.url)
                                }
                                val blocked = parsedChannels.size - channels.size
                                if (channels.isEmpty()) return@async blocked
                                val playlist = PlaylistEntity(
                                    name = name,
                                    source = category.assetPath,
                                    sourceType = SourceType.FILE.value,
                                    channelCount = channels.size
                                )
                                repository.insertPlaylistWithChannels(playlist, channels)
                                blocked
                            }
                        }
                        .awaitAll()
                        .sum()
                }
                _saveState.value = SaveState.Success(blockedCount)
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
