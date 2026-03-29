package com.dongbv.alf_03_iptv.app

import android.app.Application
import android.content.res.Resources
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ProjectApplication : Application() {
    companion object {
        var languageCodeDefault = "en"
    }

    override fun onCreate() {
        super.onCreate()
        languageCodeDefault = Resources.getSystem().configuration.locales[0].language
        startKoin {
            androidLogger()
            androidContext(this@ProjectApplication)
            modules(
                listOf(
                    dataModule,
                    viewModelModule,
                )
            )
        }
    }
}