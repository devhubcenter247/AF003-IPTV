package com.dongbv.alf_03_iptv

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.Resources
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.dongbv.alf_03_iptv.app.dataModule
import com.dongbv.alf_03_iptv.app.viewModelModule
import com.lib.ads.gma.ads.application.AdsMultiDexApplication
import com.lib.ads.gma.ads.config.AdSdkConfig
import com.lib.ads.gma.ads.util.AppLogger
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ProjectApplication : AdsMultiDexApplication(), SingletonImageLoader.Factory {
    override fun createAdSdkConfig(): AdSdkConfig = AdSdkConfig(
        this,
        if (BuildConfig.build_debug) AdSdkConfig.ENVIRONMENT_DEVELOP else AdSdkConfig.ENVIRONMENT_PRODUCTION,
    ).apply {
        listDeviceTest = listOf(
            "9B96D6E1B898DDB9D135BA906DB8CD63"
        )
        appAdId = packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        ).metaData?.getString("com.google.android.gms.ads.APPLICATION_ID")
        showMessageForTester = BuildConfig.build_debug

    }

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
        AppLogger.isEnabled= true
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