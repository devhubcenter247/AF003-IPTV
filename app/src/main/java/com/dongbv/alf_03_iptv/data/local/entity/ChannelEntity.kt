package com.dongbv.alf_03_iptv.data.local.entity

import android.os.Parcelable
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Entity representing a channel/video in a playlist.
 */
@Entity(
    tableName = "channels",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlist_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["playlist_id"]),
        Index(value = ["is_favorite"]),
        Index(value = ["group_name"])
    ]
)
@Stable
@Parcelize
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "url")
    val url: String, // Stream URL or file path

    @ColumnInfo(name = "logo")
    val logo: String? = null,

    @ColumnInfo(name = "group_name")
    val groupName: String? = null, // Category within the playlist

    @ColumnInfo(name = "epg_id")
    val epgId: String? = null, // EPG channel ID for TV guide

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "last_watched")
    val lastWatched: Long? = null,

    @ColumnInfo(name = "watch_position")
    val watchPosition: Long = 0, // Resume position in milliseconds

    @ColumnInfo(name = "duration")
    val duration: Long = 0, // Total duration in milliseconds
    @ColumnInfo(name = "categoryId")
    val  categoryId : Int = 0,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable {
    fun getFormattedDuration(): String {
        if (duration <= 0) return ""
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60
        val hours = duration / (1000 * 60 * 60)
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun getWatchProgress(): Float {
        if (duration <= 0) return 0f
        return (watchPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
    }

    companion object {
        fun fakeList(playlistId: Long = 1): List<ChannelEntity> {
            return listOf(
                ChannelEntity(
                    id = 1,
                    playlistId = playlistId,
                    name = "13Max Televisión (1080p)",
                    url = "https://live.fptplay53.net/fnxch2/vtv3hd_abr.smil/chunklist_b4200000.m3u8",
                    epgId = "13MaxTelevision.ar",
                    groupName = "News",
                    isFavorite = true
                ),
                ChannelEntity(
                    id = 2,
                    playlistId = playlistId,
                    name = "24/7 Canal de Noticias (720p)",
                    url = "https://live.fptplay53.net/fnxch2/vtv1hd_abr.smil/chunklist_b4200000.m3u8",
                    epgId = "247CanaldeNoticias.ar",
                    groupName = "News",
                    isFavorite = false
                ),
                ChannelEntity(
                    id = 3,
                    playlistId = playlistId,
                    name = "Aire de Santa Fe (1080p)",
                    url = "https://unlimited1-us.dps.live/airedesantafetv/airedesantafetv.smil/playlist.m3u8",
                    epgId = "AiredeSantaFe.ar",
                    groupName = "Regional",
                    isFavorite = true
                ),
                ChannelEntity(
                    id = 4,
                    playlistId = playlistId,
                    name = "Argentinísima Satelital (540p)",
                    url = "https://stream1.sersat.com/hls/argentinisima.m3u8",
                    epgId = "ArgentinisimaSatelital.ar",
                    groupName = "Entertainment",
                    isFavorite = false
                ),
                ChannelEntity(
                    id = 5,
                    playlistId = playlistId,
                    name = "Canal 26 (720p)",
                    url = "https://live-edge01.telecentro.net.ar/live/smil:c26.smil/playlist.m3u8",
                    epgId = "Canal26.ar",
                    groupName = "News",
                    isFavorite = true
                ),
                ChannelEntity(
                    id = 6,
                    playlistId = playlistId,
                    name = "Canal 7 Neuquén (1080p)",
                    url = "https://stream.arcast.com.ar/c7nq/ngrp:c7nq_all/playlist.m3u8",
                    epgId = "Canal7Neuquen.ar",
                    groupName = "Regional",
                    isFavorite = false
                ),
                ChannelEntity(
                    id = 7,
                    playlistId = playlistId,
                    name = "Canal E (720p)",
                    url = "https://unlimited1-us.dps.live/perfiltv/perfiltv.smil/playlist.m3u8",
                    epgId = "CanalE.ar",
                    groupName = "News",
                    isFavorite = false
                ),
                ChannelEntity(
                    id = 8,
                    playlistId = playlistId,
                    name = "El Trece (1080p)",
                    url = "https://live-01-02-eltrece.vodgc.net/eltrecetv/index.m3u8",
                    epgId = "ElTrece.ar",
                    groupName = "Entertainment",
                    isFavorite = true
                ),
                ChannelEntity(
                    id = 9,
                    playlistId = playlistId,
                    name = "NET TV (720p)",
                    url = "https://unlimited1-us.dps.live/nettv/nettv.smil/playlist.m3u8",
                    epgId = "NETTV.ar",
                    groupName = "Entertainment",
                    isFavorite = false
                ),
                ChannelEntity(
                    id = 10,
                    playlistId = playlistId,
                    name = "TN Todo Noticias (1080p)",
                    url = "https://live-01-01-tn.vodgc.net/TN24/index.m3u8",
                    epgId = "TN.ar",
                    groupName = "News",
                    isFavorite = true
                ),
                ChannelEntity(
                    id = 11,
                    playlistId = playlistId,
                    name = "Magic Kids (486p)",
                    url = "https://magicstream.ddns.net/magicstream/stream.m3u8",
                    epgId = "MagicKids.ar",
                    groupName = "Kids",
                    isFavorite = false
                ),
                ChannelEntity(
                    id = 12,
                    playlistId = playlistId,
                    name = "Pakapaka (720p)",
                    url = "https://5fb24b460df87.streamlock.net/live-cont.ar/pakapaka/playlist.m3u8",
                    epgId = "Pakapaka.ar",
                    groupName = "Kids",
                    isFavorite = true
                ),
                ChannelEntity(
                    id = 13,
                    playlistId = playlistId,
                    name = "MusicTop (720p)",
                    url = "http://live-edge01.telecentro.net.ar/live/smil:musictop.smil/playlist.m3u8",
                    epgId = "MusicTop.ar",
                    groupName = "Music",
                    isFavorite = false
                ),
                ChannelEntity(
                    id = 14,
                    playlistId = playlistId,
                    name = "Xtrema Anime (360p)",
                    url = "https://stmv1.cnarlatam.com/xtremaanime/xtremaanime/playlist.m3u8",
                    epgId = "XtremaAnime.ar",
                    groupName = "Entertainment",
                    isFavorite = true
                ),
                ChannelEntity(
                    id = 15,
                    playlistId = playlistId,
                    name = "CINE.AR (720p)",
                    url = "https://5fb24b460df87.streamlock.net/live-cont.ar/cinear/playlist.m3u8",
                    epgId = "CineAr.ar",
                    groupName = "Movies",
                    isFavorite = false
                )
            )
        }

        fun fakeSingle(playlistId: Long = 1): ChannelEntity {
            return ChannelEntity(
                id = 1,
                playlistId = playlistId,
                name = "TN Todo Noticias (1080p)",
                url = "https://live-01-01-tn.vodgc.net/TN24/index.m3u8",
                epgId = "TN.ar",
                groupName = "News",
                isFavorite = false
            )
        }
    }
}

// Shared by every screen that filters a channel list by search text and/or group
// (FavoriteViewModel, PlayListViewModel, PlayViewModel).
fun List<ChannelEntity>.filterBy(query: String, group: String? = null): List<ChannelEntity> =
    filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }
        .filter { group == null || it.groupName == group }
