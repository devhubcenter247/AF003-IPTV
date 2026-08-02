package com.dongbv.alf_03_iptv.viewmodel

import androidx.lifecycle.ViewModel
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import com.dongbv.alf_03_iptv.model.ContentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

open class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {
    private var _currentContentType = MutableStateFlow(ContentType.ALL)
    val contentType = _currentContentType.asStateFlow()

    val listHomeChannelItem = combine(
        _currentContentType,
        appRepository.getAllPlaylistsAsHomeItems().distinctUntilChanged()
    ) { type, list ->
        if (type == ContentType.ALL) {
            list
        } else {
            list.filter { it.type == type }
        }
    }


    fun selectContent(type: ContentType) {
        _currentContentType.value = type
    }


}