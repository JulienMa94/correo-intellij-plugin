package com.correomqtt.plugin.ui.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimestampHelper {
    public fun format(timestamp: String): String {
        // Trim the timestamp to keep only three milliseconds
        val trimmedTimestamp = timestamp.substring(0, 23) // yyyy-MM-ddTHH:mm:ss.SSS

        // Parse the input timestamp
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val localDateTime = LocalDateTime.parse(trimmedTimestamp, inputFormatter)

        // Convert to Date
        val instant = localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant()
        val date = Date.from(instant)

        // Format the date according to user's locale
        val dateFormatter: DateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault())

        return dateFormatter.format(date)
    }
}