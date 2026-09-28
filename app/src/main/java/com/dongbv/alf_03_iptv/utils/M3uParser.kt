package com.dongbv.alf_03_iptv.utils

import android.content.Context
import android.net.Uri
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object M3uParser {

    fun parseFromUrl(url: String, isBlocked: (String) -> Boolean = { false }): List<ChannelEntity> {
        var target = URL(url)
        repeat(6) {
            if (isBlocked(target.toString())) throw BlockedProviderUrlException()
            require(target.protocol == "http" || target.protocol == "https")
            val connection = target.openConnection() as java.net.HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            try {
                if (connection.responseCode in listOf(301, 302, 303, 307, 308)) {
                    val location = connection.getHeaderField("Location")
                        ?: error("Missing redirect location")
                    target = URL(target, location)
                } else {
                    return connection.inputStream.use { parseStream(BufferedReader(InputStreamReader(it))) }
                }
            } finally {
                connection.disconnect()
            }
        }
        error("Too many redirects")
    }

    fun parseFromAsset(context: Context, assetPath: String): List<ChannelEntity> {
        return context.assets.open(assetPath).use {
            parseStream(BufferedReader(InputStreamReader(it)))
        }
    }

    fun parseFromUri(context: Context, uri: Uri): List<ChannelEntity> {
        return context.contentResolver.openInputStream(uri)?.use {
            parseStream(BufferedReader(InputStreamReader(it)))
        } ?: emptyList()
    }

    private fun parseStream(reader: BufferedReader): List<ChannelEntity> {
        val channels = mutableListOf<ChannelEntity>()
        var currentName = ""
        var currentLogo = ""
        var currentGroup = ""
        var currentDuration = 0L

        reader.forEachLine { raw ->
            val line = raw.trim()
            when {
                line.startsWith("#EXTINF:") -> {
                    currentDuration = extractDuration(line)
                    currentName = extractDisplayName(line)
                    currentLogo = extractAttribute(line, "tvg-logo")
                    currentGroup = extractAttribute(line, "group-title")
                    if (currentName.isEmpty()) currentName = extractAttribute(line, "tvg-name")
                }
                line.isNotEmpty() && !line.startsWith("#") -> {
                    if (currentName.isNotEmpty()) {
                        channels.add(
                            ChannelEntity(
                                playlistId = 0,
                                name = currentName,
                                url = line,
                                logo = currentLogo.ifEmpty { null },
                                groupName = currentGroup.ifEmpty { "Uncategorized" },
                                duration = currentDuration
                            )
                        )
                    }
                    currentName = ""; currentLogo = ""; currentGroup = ""; currentDuration = 0L
                }
            }
        }
        return channels
    }

    private fun extractDuration(line: String): Long {
        val match = Regex("#EXTINF:(-?\\d+)").find(line)
        return match?.groupValues?.getOrNull(1)?.toLongOrNull()?.takeIf { it > 0 } ?: 0L
    }

    private fun extractDisplayName(line: String): String {
        val idx = line.lastIndexOf(',')
        return if (idx != -1 && idx < line.length - 1) line.substring(idx + 1).trim() else ""
    }

    private fun extractAttribute(line: String, attribute: String): String {
        val match = Regex("""$attribute="([^"]*)"""").find(line)
        return match?.groupValues?.getOrNull(1) ?: ""
    }
}

class BlockedProviderUrlException : IllegalArgumentException("Blocked provider URL")
