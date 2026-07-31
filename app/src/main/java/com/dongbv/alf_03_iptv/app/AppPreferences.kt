package com.dongbv.alf_03_iptv.app

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppPreferences : KoinComponent {
    private val sharedPreference: SharedPreference by inject()
    var languageCode by sharedPreference.string("languageCode", "en")
        private set
    var isFinishFirstFlow by sharedPreference.boolean("isFinishFirstFlow", false)
    var firstOpenApp by sharedPreference.int("firstOpenApp", 0)
    fun isUfo(): Boolean {
        return firstOpenApp == 1
    }
    val isRatingApp = sharedPreference.observable("isRatingApp", false)
    val isEnableSound = sharedPreference.observable("isEnableSound", false)
    val isEnableVibrate = sharedPreference.observable("isEnableVibrate", false)

    fun setLocale(language: String = "en") {
        languageCode = language
    }
}