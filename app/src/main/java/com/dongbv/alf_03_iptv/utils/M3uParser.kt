package com.dongbv.alf_03_iptv.utils

import android.content.Context
import android.net.Uri
import com.dongbv.alf_03_iptv.data.local.entity.ChannelEntity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object M3uParser {

    fun parseFromUrl(url: String): List<ChannelEntity> {
        val connection = URL(url).openConnection()
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        return connection.getInputStream().use { parseStream(BufferedReader(InputStreamReader(it))) }
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
