package com.dongbv.alf_03_iptv.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dongbv.alf_03_iptv.data.local.dao.ChannelDao
import com.dongbv.alf_03_iptv.data.local.dao.PlaylistDao
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity

// The single instance is built once, in Koin's dataModule (see IModule.kt), and injected
// everywhere else — do not add a second singleton-builder here.
@Database(
    entities = [
        PlaylistEntity::class,
        ChannelEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun channelDao(): ChannelDao
}
