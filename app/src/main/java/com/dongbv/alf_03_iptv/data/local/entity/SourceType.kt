package com.dongbv.alf_03_iptv.data.local.entity

/**
 * Enum representing the source type of a playlist.
 * Used for filtering playlists by category on the Home screen.
 */
enum class SourceType(val value: Int) {
    URL(0),     
    FILE(1),    
    GALLERY(2); 

    companion object {
        fun fromValue(value: Int): SourceType {
            return entries.find { it.value == value } ?: URL
        }
    }
}
