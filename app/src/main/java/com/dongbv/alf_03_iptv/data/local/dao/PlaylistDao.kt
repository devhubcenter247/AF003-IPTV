package com.dongbv.alf_03_iptv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

   
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(playlists: List<PlaylistEntity>)

   
    @Update
    suspend fun update(playlist: PlaylistEntity)

    @Query("UPDATE playlists SET channel_count = :count, last_updated = :timestamp WHERE id = :playlistId")
    suspend fun updateChannelCount(playlistId: Long, count: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE playlists SET name = :name WHERE id = :playlistId")
    suspend fun updateName(playlistId: Long, name: String)

    @Query("UPDATE playlists SET is_active = :isActive WHERE id = :playlistId")
    suspend fun updateActiveStatus(playlistId: Long, isActive: Boolean)

    @Query("UPDATE playlists SET last_watched = :timestamp WHERE id = :playlistId")
    suspend fun updateLastWatched(playlistId: Long, timestamp: Long = System.currentTimeMillis())

   
    @Delete
    suspend fun delete(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deleteById(playlistId: Long)

    @Query("DELETE FROM playlists")
    suspend fun deleteAll()

   
    @Query("SELECT * FROM playlists WHERE is_active = 1 ORDER BY created_at DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE source_type = :sourceType AND is_active = 1 ORDER BY created_at DESC")
    fun getPlaylistsBySourceType(sourceType: Int): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): Flow<PlaylistEntity?>

    @Query("SELECT * FROM playlists WHERE name LIKE '%' || :query || '%' AND is_active = 1 ORDER BY created_at DESC")
    fun searchPlaylists(query: String): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE last_watched IS NOT NULL AND is_active = 1 ORDER BY last_watched DESC LIMIT :limit")
    fun getRecentlyWatched(limit: Int = 50): Flow<List<PlaylistEntity>>

   
    @Query("SELECT * FROM playlists WHERE is_active = 1 ORDER BY created_at DESC")
    suspend fun getAllPlaylistsOnce(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE source_type = :sourceType AND is_active = 1 ORDER BY created_at DESC")
    suspend fun getPlaylistsBySourceTypeOnce(sourceType: Int): List<PlaylistEntity>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistByIdOnce(playlistId: Long): PlaylistEntity?

    @Query("SELECT * FROM playlists WHERE source = :source LIMIT 1")
    suspend fun getPlaylistBySource(source: String): PlaylistEntity?

    @Query("SELECT COUNT(*) FROM playlists WHERE is_active = 1")
    suspend fun getPlaylistCount(): Int

    @Query("SELECT COUNT(*) FROM playlists WHERE source_type = :sourceType AND is_active = 1")
    suspend fun getPlaylistCountBySourceType(sourceType: Int): Int

   
    @Query("SELECT EXISTS(SELECT 1 FROM playlists WHERE source = :source LIMIT 1)")
    suspend fun playlistExists(source: String): Boolean
}
