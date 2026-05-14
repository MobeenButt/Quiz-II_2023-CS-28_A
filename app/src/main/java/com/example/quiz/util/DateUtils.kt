package com.example.quiz.util

import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    // Currents API format: "2026-03-24 11:10:00 +0000"
    private val currentsInputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH)

    // ISO 8601 fallback (GNews format)
    private val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.getDefault())

    fun format(dateStr: String): String {
        return runCatching {
            // Try Currents format first
            val parsed = ZonedDateTime.parse(dateStr, currentsInputFormat)
            parsed.format(outputFormat)
        }.recoverCatching {
            // Fallback to ISO 8601
            val parsed = OffsetDateTime.parse(dateStr)
            parsed.format(outputFormat)
        }.getOrElse { dateStr }
    }
}

