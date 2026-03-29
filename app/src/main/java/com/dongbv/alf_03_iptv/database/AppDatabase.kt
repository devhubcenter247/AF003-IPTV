package com.dongbv.alf_03_iptv.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dongbv.alf_03_iptv.database.model.HistoryApp

@Database(entities = [HistoryApp::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun history(): HistoryDao
}