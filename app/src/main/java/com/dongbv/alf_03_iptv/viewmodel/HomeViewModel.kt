package com.dongbv.alf_03_iptv.viewmodel

import androidx.lifecycle.ViewModel
import com.dongbv.alf_03_iptv.database.AppRepository
import com.dongbv.alf_03_iptv.model.HomeChannelItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {

    private val _listHomeChannel = MutableStateFlow<List<HomeChannelItem>>(emptyList())
    val listHomeChannel = _listHomeChannel.asStateFlow()


}