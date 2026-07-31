package com.dongbv.alf_03_iptv.app

import android.app.Application
import android.content.res.Resources
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ProjectApplication : Application(), SingletonImageLoader.Factory {
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

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache").toOkioPath())
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }
}