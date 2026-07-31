package com.dongbv.alf_03_iptv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.local.entity.filterBy
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: AppRepository) : ViewModel() {

    // 0 = Favorites, 1 = History, 2 = All Channels
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGroup = MutableStateFlow<String?>(null)
    val selectedGroup: StateFlow<String?> = _selectedGroup.asStateFlow()

    private val _isGridView = MutableStateFlow(true)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    private val rawFavorites = repository.getFavoriteChannels()
    private val rawHistory  = repository.getRecentlyWatched()
    private val rawAll      = repository.getAllChannels()

    val favoriteChannels: StateFlow<List<ChannelEntity>> = combine(
        rawFavorites, _searchQuery, _selectedGroup
    ) { list, q, g -> list.filterBy(q, g) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val historyChannels: StateFlow<List<ChannelEntity>> = combine(
        rawHistory, _searchQuery, _selectedGroup
    ) { list, q, g -> list.filterBy(q, g) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val allChannels: StateFlow<List<ChannelEntity>> = combine(
        rawAll, _searchQuery, _selectedGroup
    ) { list, q, g -> list.filterBy(q, g) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val availableGroups: StateFlow<List<String>> = combine(
        favoriteChannels, historyChannels, allChannels, _selectedTab
    ) { favorites, history, all, tab ->
        val list = when (tab) { 0 -> favorites; 1 -> history; else -> all }
        list.mapNotNull { it.groupName }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setTab(tab: Int) {
        _selectedTab.value = tab
        _selectedGroup.value = null
        _searchQuery.value = ""
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setGroupFilter(group: String?) { _selectedGroup.value = group }
    fun toggleLayout() { _isGridView.value = !_isGridView.value }
    fun toggleFavorite(id: Long) = viewModelScope.launch { repository.toggleFavorite(id) }
    fun removeFromHistory(id: Long) = viewModelScope.launch { repository.removeChannelFromHistory(id) }
}
