package com.dongbv.alf_03_iptv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

   
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(channel: ChannelEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(channels: List<ChannelEntity>)

   
    @Update
    suspend fun update(channel: ChannelEntity)

    @Query("UPDATE channels SET is_favorite = :isFavorite WHERE id = :channelId")
    suspend fun updateFavoriteStatus(channelId: Long, isFavorite: Boolean)

    @Query("UPDATE channels SET is_favorite = CASE WHEN is_favorite = 1 THEN 0 ELSE 1 END WHERE id = :channelId")
    suspend fun toggleFavorite(channelId: Long)

    @Query("UPDATE channels SET last_watched = :timestamp, watch_position = :position WHERE id = :channelId")
    suspend fun updateWatchProgress(
          channelId: Long,
          position: Long,
          timestamp: Long = System.currentTimeMillis()
    )

    @Query("UPDATE channels SET last_watched = :timestamp WHERE id = :channelId")
    suspend fun updateLastWatchHistory(
          channelId: Long,
          timestamp: Long = System.currentTimeMillis()
    )
    @Query("UPDATE channels SET last_watched = NULL WHERE id = :channelId")
    suspend fun removeFromHistory(channelId: Long)
    @Query("UPDATE channels SET duration = :duration WHERE id = :channelId")
    suspend fun updateDuration(channelId: Long, duration: Long)

    @Query("UPDATE channels SET name =:newName WHERE id =:channelId")
    suspend fun renameChannel(channelId: Long, newName: String)

   
    @Delete
    suspend fun delete(channel: ChannelEntity)

    @Query("DELETE FROM channels WHERE id = :channelId")
    suspend fun deleteById(channelId: Long)

    @Query("DELETE FROM channels WHERE playlist_id = :playlistId")
    suspend fun deleteByPlaylistId(playlistId: Long)

    @Query("DELETE FROM channels")
    suspend fun deleteAll()

   
    @Query("SELECT * FROM channels WHERE playlist_id = :playlistId ORDER BY name ASC")
    fun getChannelsByPlaylist(playlistId: Long): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE playlist_id = :playlistId AND group_name = :groupName ORDER BY name ASC")
    fun getChannelsByGroup(playlistId: Long, groupName: String): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels ORDER BY name ASC")
    fun getAllChannels(): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE is_favorite = 1 ORDER BY name ASC")
    fun getFavoriteChannels(): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE last_watched IS NOT NULL ORDER BY last_watched DESC LIMIT :limit")
    fun getRecentlyWatched(limit: Int = 50): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM channels WHERE id = :channelId")
    fun getChannelById(channelId: Long): Flow<ChannelEntity?>

    @Query("SELECT * FROM channels WHERE playlist_id = :playlistId AND name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchChannels(playlistId: Long, query: String): Flow<List<ChannelEntity>>

    @Query("SELECT DISTINCT group_name FROM channels WHERE playlist_id = :playlistId AND group_name IS NOT NULL ORDER BY group_name ASC")
    fun getGroupsByPlaylist(playlistId: Long): Flow<List<String>>

   
    @Query("SELECT * FROM channels WHERE playlist_id = :playlistId ORDER BY name ASC")
    suspend fun getChannelsByPlaylistOnce(playlistId: Long): List<ChannelEntity>

    @Query("SELECT * FROM channels WHERE is_favorite = 1 ORDER BY name ASC")
    suspend fun getFavoriteChannelsOnce(): List<ChannelEntity>

    @Query("SELECT * FROM channels WHERE last_watched IS NOT NULL ORDER BY last_watched DESC LIMIT :limit")
    suspend fun getRecentlyWatchedOnce(limit: Int = 50): List<ChannelEntity>

    @Query("SELECT * FROM channels WHERE id = :channelId")
    suspend fun getChannelByIdOnce(channelId: Long): ChannelEntity?

    @Query("SELECT * FROM channels WHERE url = :url LIMIT 1")
    suspend fun getChannelByUrl(url: String): ChannelEntity?

    @Query("SELECT COUNT(*) FROM channels WHERE playlist_id = :playlistId")
    suspend fun getChannelCount(playlistId: Long): Int

    @Query("SELECT COUNT(*) FROM channels WHERE is_favorite = 1")
    suspend fun getFavoriteCount(): Int

   
    @Query("SELECT EXISTS(SELECT 1 FROM channels WHERE playlist_id = :playlistId AND url = :url LIMIT 1)")
    suspend fun channelExists(playlistId: Long, url: String): Boolean
}
