package com.dongbv.alf_03_iptv.data.local

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dongbv.alf_03_iptv.data.local.dao.ChannelDao
import com.dongbv.alf_03_iptv.data.local.dao.PlaylistDao
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import com.dongbv.alf_03_iptv.data.local.entity.PlaylistEntity

@Database(
    entities = [
        PlaylistEntity::class,
        ChannelEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun channelDao(): ChannelDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val hasCategoryId = db.query("PRAGMA table_info(channels)").use { cursor ->
                    val nameIndex = cursor.getColumnIndex("name")
                    generateSequence {
                        if (cursor.moveToNext()) cursor.getString(nameIndex) else null
                    }.any { it == "categoryId" }
                }
                if (!hasCategoryId) {
                    db.execSQL(
                        "ALTER TABLE channels ADD COLUMN categoryId INTEGER NOT NULL DEFAULT 0"
                    )
                }
            }
        }
    }
}
