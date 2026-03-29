package com.dongbv.alf_03_iptv.app

import androidx.room.Room
import com.dongbv.alf_03_iptv.database.AppDatabase
import com.dongbv.alf_03_iptv.database.AppRepository
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
}
val dataModule = module {
    singleOf(::SharedPreference)
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "${androidContext().applicationContext.packageName}_history_db"
        ).build()
    }
    single { get<AppDatabase>().history() }
    singleOf(::AppRepository)
}
