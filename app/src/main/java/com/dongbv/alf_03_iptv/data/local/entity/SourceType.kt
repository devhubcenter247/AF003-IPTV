package com.dongbv.alf_03_iptv.data.local.entity

/**
 * Enum representing the source type of a playlist.
 * Used for filtering playlists by category on the Home screen.
 */
enum class SourceType(val value: Int) {
    URL(0),      // Playlist loaded from a URL (M3U, etc.)
    FILE(1),     // Playlist loaded from a local file
    GALLERY(2);  // Videos from device gallery

    companion object {
        fun fromValue(value: Int): SourceType {
            return entries.find { it.value == value } ?: URL
        }
    }
}
