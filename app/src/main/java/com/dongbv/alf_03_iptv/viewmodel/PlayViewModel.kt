package com.dongbv.alf_03_iptv.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.local.entity.filterBy
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

sealed class StreamCheckResult {
    object Idle : StreamCheckResult()
    data class Checking(val channelId: Long) : StreamCheckResult()
    data class Success(val channelId: Long, val httpCode: Int) : StreamCheckResult()
    data class Failed(val channelId: Long, val httpCode: Int, val error: String) : StreamCheckResult()
}

class PlayViewModel(private val repository: AppRepository) : ViewModel() {

    private val _streamCheckResult = MutableStateFlow<StreamCheckResult>(StreamCheckResult.Idle)
    val streamCheckResult: StateFlow<StreamCheckResult> = _streamCheckResult.asStateFlow()

    private var preloadJob: Job? = null

    private val _channels = MutableStateFlow<List<ChannelEntity>>(emptyList())
    val channels: StateFlow<List<ChannelEntity>> = _channels.asStateFlow()

    private val _currentChannel = MutableStateFlow<ChannelEntity?>(null)
    val currentChannel: StateFlow<ChannelEntity?> = _currentChannel.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private var allChannels: List<ChannelEntity> = emptyList()

    private var _searchQuery: String = ""

    private var pendingChannelId: Long? = null

    fun loadChannelsByPlaylist(playlistId: Long) {
        viewModelScope.launch {
            repository.getChannelsByPlaylist(playlistId).collect { list ->
                allChannels = list
                _channels.value = list.filterBy(_searchQuery)
                pendingChannelId?.let { id ->
                    list.find { it.id == id }?.let {
                        setCurrentChannel(it)
                        pendingChannelId = null
                    }
                }
            }
        }
    }

    fun searchChannels(query: String) {
        _searchQuery = query
        _channels.value = allChannels.filterBy(query)
    }

    fun setCurrentChannelById(channelId: Long) {
        val channel = allChannels.find { it.id == channelId }
        if (channel != null) {
            setCurrentChannel(channel)
        } else {
            pendingChannelId = channelId
        }
    }

    fun setCurrentChannel(channel: ChannelEntity) {
        _currentChannel.value = channel
        _isFavorite.value = channel.isFavorite
        viewModelScope.launch {
            repository.updateChannelLastWatchHistory(channel.id)
        }
    }

    fun getPreviousChannel(): ChannelEntity? {
        val current = _currentChannel.value ?: return null
        val idx = allChannels.indexOfFirst { it.id == current.id }
        return if (idx > 0) allChannels[idx - 1] else null
    }

    fun getNextChannel(): ChannelEntity? {
        val current = _currentChannel.value ?: return null
        val idx = allChannels.indexOfFirst { it.id == current.id }
        return if (idx in 0 until allChannels.lastIndex) allChannels[idx + 1] else null
    }

    fun toggleFavorite() {
        val channel = _currentChannel.value ?: return
        val newFav = !_isFavorite.value
        _isFavorite.value = newFav
        _currentChannel.value = channel.copy(isFavorite = newFav)
        viewModelScope.launch {
            repository.setFavorite(channel.id, newFav)
        }
    }

    fun preloadCheck(channel: ChannelEntity) {
        preloadJob?.cancel()
        _streamCheckResult.value = StreamCheckResult.Checking(channel.id)
        preloadJob = viewModelScope.launch(Dispatchers.IO) {
            var code = -1
            try {
                val conn = (URL(channel.url).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5_000
                    readTimeout = 5_000
                    instanceFollowRedirects = true
                }
                code = conn.responseCode
                conn.disconnect()
                Log.d(TAG, "preload OK  [${channel.id}] ${channel.name} → HTTP $code  url=${channel.url}")
                _streamCheckResult.value = StreamCheckResult.Success(channel.id, code)
            } catch (e: Exception) {
                val msg = "${e.javaClass.simpleName}: ${e.message}"
                Log.e(TAG, "preload ERR [${channel.id}] ${channel.name} → $msg  url=${channel.url}", e)
                _streamCheckResult.value = StreamCheckResult.Failed(channel.id, code, msg)
            }
        }
    }

    fun updateWatchProgress(position: Long) {
        val channel = _currentChannel.value ?: return
        viewModelScope.launch {
            repository.updateChannelWatchProgress(channel.id, position)
        }
    }

    companion object {
        private const val TAG = "PlayViewModel"
    }
}
