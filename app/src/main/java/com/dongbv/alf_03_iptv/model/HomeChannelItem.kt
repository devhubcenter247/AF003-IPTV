package com.dongbv.alf_03_iptv.model

import androidx.annotation.DrawableRes
import com.dongbv.alf_03_iptv.R

enum class ContentType {
    ALL, URL, FILE, GALLERY
}
data class HomeChannelItem(
    val id: Int,
    val title: String,
    val count: Int,
    val type: ContentType,
    @DrawableRes val icon: Int
)

val listDemoContent
    get() = listOf(
        HomeChannelItem(1, "My Playlist", 12, ContentType.URL, R.drawable.ic_app_url),
        HomeChannelItem(2, "Sports Channel", 8, ContentType.URL, R.drawable.ic_app_url),
        HomeChannelItem(3, "Movies Collection", 25, ContentType.FILE, R.drawable.ic_app_video),
        HomeChannelItem(4, "News Channels", 15, ContentType.GALLERY, R.drawable.ic_app_storage),
        HomeChannelItem(5, "Music & Entertainment", 20, ContentType.FILE, R.drawable.ic_app_url),
        HomeChannelItem(6, "Documentary", 10, ContentType.GALLERY, R.drawable.ic_app_url),
        HomeChannelItem(7, "Kids Channel", 18, ContentType.URL, R.drawable.ic_app_url)
    )
