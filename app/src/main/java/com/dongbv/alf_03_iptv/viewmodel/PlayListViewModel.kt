package com.dongbv.alf_03_iptv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity
import com.dongbv.alf_03_iptv.data.local.entity.filterBy
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val playlistId: Long,
    private val repository: AppRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGroup = MutableStateFlow<String?>(null)
    val selectedGroup: StateFlow<String?> = _selectedGroup.asStateFlow()

    private val _isGridView = MutableStateFlow(true)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    val playlist: StateFlow<PlaylistEntity?> = repository.getPlaylistById(playlistId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val rawChannels = repository.getChannelsByPlaylist(playlistId)

    val channels: StateFlow<List<ChannelEntity>> = combine(
        rawChannels, _searchQuery, _selectedGroup
    ) { list, query, group ->
        list.filterBy(query, group)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val availableGroups: StateFlow<List<String>> = rawChannels
        .combine(_searchQuery) { list, _ -> list.mapNotNull { it.groupName }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setGroupFilter(group: String?) { _selectedGroup.value = group }
    fun toggleLayout() { _isGridView.value = !_isGridView.value }
    fun toggleFavorite(id: Long) = viewModelScope.launch { repository.toggleFavorite(id) }
}
