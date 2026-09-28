package com.dongbv.alf_03_iptv.app

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AppPreferences : KoinComponent {
    private val sharedPreference: SharedPreference by inject()
    var languageCode by sharedPreference.string("languageCode", "en")
        private set
    var isFinishFirstFlow by sharedPreference.boolean("isFinishFirstFlow", false)
    var firstOpenApp by sharedPreference.int("firstOpenApp", 0)
    var playerCloseCount by sharedPreference.int("playerCloseCount", 0)
    private var rateAfterPlaylistImport by sharedPreference.boolean("rateAfterPlaylistImport", false)
    private var importedPlaylistSources by sharedPreference.string("importedPlaylistSources", "")
    fun isUfo(): Boolean {
        return firstOpenApp == 1
    }
    val isRatingApp = sharedPreference.observable("isRatingApp", false)
    val isEnableSound = sharedPreference.observable("isEnableSound", false)
    val isEnableVibrate = sharedPreference.observable("isEnableVibrate", false)

    fun setLocale(language: String = "en") {
        languageCode = language
    }

    fun registerPlayerClose(): Boolean {
        playerCloseCount += 1
        if (playerCloseCount < 3) return false
        playerCloseCount = 0
        return true
    }

    fun hasImportedPlaylist(source: String) = importedPlaylistSources.split('\n').any { it == source }
    fun registerImportedPlaylist(source: String) {
        if (!hasImportedPlaylist(source)) {
            importedPlaylistSources = (importedPlaylistSources.split('\n').filter { it.isNotBlank() } + source).joinToString("\n")
        }
    }
    fun importedPlaylistCount() = importedPlaylistSources.split('\n').count { it.isNotBlank() }

    fun requestRateAfterPlaylistImport() {
        if (!isRatingApp.value) rateAfterPlaylistImport = true
    }

    fun consumeRateAfterPlaylistImport(): Boolean {
        if (isRatingApp.value || !rateAfterPlaylistImport) return false
        rateAfterPlaylistImport = false
        return true
    }
}
