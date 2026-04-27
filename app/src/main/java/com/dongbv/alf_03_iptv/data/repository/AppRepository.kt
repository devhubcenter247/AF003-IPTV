package com.dongbv.alf_03_iptv.data.repository

import com.dongbv.alf_03_iptv.R
import com.dongbv.alf_03_iptv.data.local.dao.ChannelDao
import com.dongbv.alf_03_iptv.data.local.dao.PlaylistDao
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity
import com.dongbv.alf_03_iptv.data.local.entity.SourceType
import com.dongbv.alf_03_iptv.model.ContentType
import com.dongbv.alf_03_iptv.model.HomeChannelItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(private val channelDao: ChannelDao, private val playlistDao: PlaylistDao) {

    fun getAllPlaylists(): Flow<List<PlaylistEntity>> = playlistDao.getAllPlaylists()

    fun getAllPlaylistsAsHomeItems(): Flow<List<HomeChannelItem>> =
        playlistDao.getAllPlaylists().map { playlists ->
            playlists.map { it.toHomeChannelItem() }
        }

    fun getPlaylistsBySourceType(sourceType: SourceType): Flow<List<PlaylistEntity>> =
        playlistDao.getPlaylistsBySourceType(sourceType.value)

    fun getPlaylistsBySourceTypeAsHomeItems(sourceType: SourceType): Flow<List<HomeChannelItem>> =
        playlistDao.getPlaylistsBySourceType(sourceType.value).map { playlists ->
            playlists.map { it.toHomeChannelItem() }
        }

    fun getPlaylistsByContentType(contentType: ContentType): Flow<List<HomeChannelItem>> {
        return if (contentType == ContentType.ALL) {
            getAllPlaylistsAsHomeItems()
        } else {
            getPlaylistsBySourceTypeAsHomeItems(contentType.toSourceType())
        }
    }

    fun getPlaylistById(playlistId: Long): Flow<PlaylistEntity?> =
        playlistDao.getPlaylistById(playlistId)

    fun searchPlaylists(query: String): Flow<List<PlaylistEntity>> =
        playlistDao.searchPlaylists(query)

    fun getRecentlyWatchedPlaylists(limit: Int = 50): Flow<List<PlaylistEntity>> =
        playlistDao.getRecentlyWatched(limit)

    fun getRecentlyWatchedPlaylistsAsHomeItems(limit: Int = 50): Flow<List<HomeChannelItem>> =
        playlistDao.getRecentlyWatched(limit).map { playlists ->
            playlists.map { it.toHomeChannelItem() }
        }

    suspend fun updatePlaylistLastWatched(playlistId: Long) =
        playlistDao.updateLastWatched(playlistId)

    suspend fun insertPlaylist(playlist: PlaylistEntity): Long =
        playlistDao.insert(playlist)

    suspend fun insertPlaylistWithChannels(
        playlist: PlaylistEntity,
        channels: List<ChannelEntity>
    ): Long {
        val playlistId = playlistDao.insert(playlist)
        val channelsWithPlaylistId = channels.map { it.copy(playlistId = playlistId) }
        channelDao.insertAll(channelsWithPlaylistId)
        playlistDao.updateChannelCount(playlistId, channelsWithPlaylistId.size)
        return playlistId
    }

    suspend fun updatePlaylist(playlist: PlaylistEntity) =
        playlistDao.update(playlist)

    suspend fun updatePlaylistName(playlistId: Long, name: String) =
        playlistDao.updateName(playlistId, name)

    suspend fun deletePlaylist(playlistId: Long) =
        playlistDao.deleteById(playlistId)

    suspend fun playlistExists(source: String): Boolean =
        playlistDao.playlistExists(source)

    suspend fun getPlaylistBySource(source: String): PlaylistEntity? =
        playlistDao.getPlaylistBySource(source)

    // channel


    fun getChannelsByPlaylist(playlistId: Long): Flow<List<ChannelEntity>> =
        channelDao.getChannelsByPlaylist(playlistId)

    fun getChannelsByGroup(playlistId: Long, groupName: String): Flow<List<ChannelEntity>> =
        channelDao.getChannelsByGroup(playlistId, groupName)

    fun getAllChannels(): Flow<List<ChannelEntity>> =
        channelDao.getAllChannels()

    fun getFavoriteChannels(): Flow<List<ChannelEntity>> =
        channelDao.getFavoriteChannels()

    fun getRecentlyWatched(limit: Int = 50): Flow<List<ChannelEntity>> =
        channelDao.getRecentlyWatched(limit)

    fun getChannelById(channelId: Long): Flow<ChannelEntity?> =
        channelDao.getChannelById(channelId)

    fun searchChannels(playlistId: Long, query: String): Flow<List<ChannelEntity>> =
        channelDao.searchChannels(playlistId, query)

    fun getGroupsByPlaylist(playlistId: Long): Flow<List<String>> =
        channelDao.getGroupsByPlaylist(playlistId)

    suspend fun insertChannels(channels: List<ChannelEntity>) {
        channelDao.insertAll(channels)
        channels.groupBy { it.playlistId }.forEach { (playlistId, _) ->
            val count = channelDao.getChannelCount(playlistId)
            playlistDao.updateChannelCount(playlistId, count)
        }
    }

    suspend fun toggleFavorite(channelId: Long) {
        val channel = channelDao.getChannelByIdOnce(channelId)
        channel?.let {
            channelDao.updateFavoriteStatus(channelId, !it.isFavorite)
        }
    }
    suspend fun renameChannel(channelId: Long, newName: String ) = channelDao.renameChannel(channelId,newName)

    suspend fun setFavorite(channelId: Long, isFavorite: Boolean) =
        channelDao.updateFavoriteStatus(channelId, isFavorite)

    suspend fun updateChannelWatchProgress(channelId: Long, position: Long) =
        channelDao.updateWatchProgress(channelId, position)

    suspend fun updateChannelLastWatchHistory(channelId: Long) =
        channelDao.updateLastWatchHistory(channelId)
    suspend fun removeChannelFromHistory(channelId: Long) =
        channelDao.removeFromHistory(channelId)

    suspend fun deleteChannel(channelId: Long) {
        val channel = channelDao.getChannelByIdOnce(channelId)
        channelDao.deleteById(channelId)
        channel?.let {
            val count = channelDao.getChannelCount(it.playlistId)
            if (count == 0) {
                playlistDao.deleteById(it.playlistId)
            } else {
                playlistDao.updateChannelCount(it.playlistId, count)
            }
        }
    }

    suspend fun deleteChannelsByPlaylist(playlistId: Long) {
        channelDao.deleteByPlaylistId(playlistId)
        playlistDao.updateChannelCount(playlistId, 0)
    }

    suspend fun refreshPlaylistChannels(playlistId: Long, channels: List<ChannelEntity>) {
        channelDao.deleteByPlaylistId(playlistId)
        val channelsWithPlaylistId = channels.map { it.copy(playlistId = playlistId) }
        channelDao.insertAll(channelsWithPlaylistId)
        playlistDao.updateChannelCount(playlistId, channelsWithPlaylistId.size)
    }

    suspend fun getPlaylistCount(): Int = playlistDao.getPlaylistCount()

    suspend fun getPlaylistCountBySourceType(sourceType: SourceType): Int =
        playlistDao.getPlaylistCountBySourceType(sourceType.value)

    suspend fun getFavoriteCount(): Int = channelDao.getFavoriteCount()
}

fun PlaylistEntity.toHomeChannelItem(): HomeChannelItem {
    val contentType = when (SourceType.fromValue(sourceType)) {
        SourceType.URL -> ContentType.URL
        SourceType.FILE -> ContentType.FILE
        SourceType.GALLERY -> ContentType.GALLERY
    }

    val icon = when (SourceType.fromValue(sourceType)) {
        SourceType.URL -> R.drawable.ic_app_url
        SourceType.FILE -> R.drawable.ic_app_file
        SourceType.GALLERY -> R.drawable.ic_app_storage
    }

    return HomeChannelItem(
        id = id.toInt(),
        title = name,
        count = channelCount,
        type = contentType,
        icon = icon
    )
}

fun ContentType.toSourceType(): SourceType {
    return when (this) {
        ContentType.ALL -> SourceType.URL
        ContentType.URL -> SourceType.URL
        ContentType.FILE -> SourceType.FILE
        ContentType.GALLERY -> SourceType.GALLERY
    }
}

fun SourceType.toContentType(): ContentType {
    return when (this) {
        SourceType.URL -> ContentType.URL
        SourceType.FILE -> ContentType.FILE
        SourceType.GALLERY -> ContentType.GALLERY
    }
}
