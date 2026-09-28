package com.dongbv.alf_03_iptv.utils

import java.net.URLDecoder
import java.text.Normalizer
import java.util.Locale

/** Conservative substring matching, including spaced and URL-encoded provider names. */
class BlockedProviderPolicy(keywords: List<String>) {
    private val blocked = keywords.map(::normalize).filter { it.isNotEmpty() }.distinct()

    fun isBlocked(value: String): Boolean {
        val normalized = normalize(value)
        return blocked.any(normalized::contains)
    }

    private fun normalize(value: String): String {
        // Preserve literal '+' while decoding percent-encoded URL characters.
        val decoded = try {
            URLDecoder.decode(value.replace("+", "%2B"), "UTF-8")
        } catch (_: IllegalArgumentException) {
            value
        }
        return Normalizer.normalize(decoded, Normalizer.Form.NFKC)
            .lowercase(Locale.ROOT)
            .filter { it.isLetterOrDigit() }
    }
}
