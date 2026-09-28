package com.dongbv.alf_03_iptv.app

import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.room.Room
import com.dongbv.alf_03_iptv.data.local.AppDatabase
import com.dongbv.alf_03_iptv.data.repository.AppRepository
import com.dongbv.alf_03_iptv.viewmodel.AddItemViewModel
import com.dongbv.alf_03_iptv.viewmodel.FavoriteViewModel
import com.dongbv.alf_03_iptv.viewmodel.HomeViewModel
import com.dongbv.alf_03_iptv.viewmodel.PersonalizationViewModel
import com.dongbv.alf_03_iptv.viewmodel.PlayListViewModel
import com.dongbv.alf_03_iptv.viewmodel.PlayViewModel
import com.dongbv.alf_03_iptv.viewmodel.WatchNowViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.io.File

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddItemViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::PlayViewModel)
    viewModel { params -> PlayListViewModel(params.get(), get()) }
    viewModel { PersonalizationViewModel(androidApplication(), get(), get()) }
    viewModel { WatchNowViewModel(androidApplication(), get(), get()) }
}

val dataModule = module {
   single { SharedPreference(androidContext()) }
    single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "iptv_database"
            )
                .addMigrations(AppDatabase.MIGRATION_2_3)
                .fallbackToDestructiveMigration(false)
                .build()
    }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().channelDao() }
    single { AppRepository(get(), get(), get()) }
    single {
        com.dongbv.alf_03_iptv.utils.BlockedProviderPolicy(
            androidContext().resources.getStringArray(com.dongbv.alf_03_iptv.R.array.blocked_provider_keywords).toList()
        )
    }
    single {
        SimpleCache(
            File(androidContext().cacheDir, "stream_cache"),
            LeastRecentlyUsedCacheEvictor(100L * 1024 * 1024),
            StandaloneDatabaseProvider(androidContext())
        )
    }
}
