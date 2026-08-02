package com.dongbv.alf_03_iptv.app.remoteconfig

val remoteConfig get() = RemoteConfig.Instance

class RemoteConfig : RemoteConfigBase() {

    companion object {
        val Instance: RemoteConfig by lazy { RemoteConfig() }
    }

    private val defaultTimeNotiLock = listOf(3 to 0, 8 to 0, 13 to 0, 18 to 0, 23 to 0)
    val timeNotiLock by remoteCustom("time_noti_reminder", defaultTimeNotiLock) { raw ->
        notiTimesRemoteConfig(raw, defaultTimeNotiLock)
    }

    private fun notiTimesRemoteConfig(
        rawValue: String,
        default: List<Pair<Int, Int>>,
    ): List<Pair<Int, Int>> {
        if (rawValue.isBlank()) return default
        val result = rawValue.split(",")
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { timeStr ->
                val parts = timeStr.split(":")
                if (parts.size != 2) return@mapNotNull null
                val hour = parts[0].toIntOrNull() ?: return@mapNotNull null
                val minute = parts[1].toIntOrNull() ?: return@mapNotNull null
                if (hour !in 0..23 || minute !in 0..59) return@mapNotNull null
                hour to minute
            }
            .distinct()
            .sortedWith(compareBy<Pair<Int, Int>> { it.first }.thenBy { it.second })
            .toList()
        return result.ifEmpty { default }
    }
}
