package com.dongbv.alf_03_iptv.app

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

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddItemViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::PlayViewModel)
    viewModel { params -> PlayListViewModel(params.get(), get()) }
    viewModel { PersonalizationViewModel(androidApplication(), get()) }
    viewModel { WatchNowViewModel(androidApplication(), get()) }
}

val dataModule = module {
   single { SharedPreference(androidContext()) }
    single {
        Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "iptv_database"
            ).fallbackToDestructiveMigration(false).build()
    }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().channelDao() }
    single { AppRepository(get(), get()) }
}
