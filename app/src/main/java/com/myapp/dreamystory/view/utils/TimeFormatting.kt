package com.myapp.dreamystory.view.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

fun String.toRelativeTime(): String {
    val timeInputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    timeInputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = timeInputFormat.parse(this) ?: return "Invalid date"

    val now = Date()
    val diffInMillis = now.time - date.time

    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    val diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

    val relativeTime = when {
        diffInMinutes < 1 -> "just now"
        diffInMinutes < 60 -> "$diffInMinutes minutes ago"
        diffInHours < 24 -> "$diffInHours hours ago"
        diffInDays < 7 -> "$diffInDays days ago"
        else -> {
            val timeOutputFormat = SimpleDateFormat("dd MMMM yyyy (HH:mm)", Locale.US)
            timeOutputFormat.format(date)
        }
    }

    val timeOutputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US)
    val formattedDate = timeOutputFormat.format(date)

    return if (relativeTime.isNotEmpty()) {
        "$formattedDate ($relativeTime)"
    } else {
        formattedDate
    }
}
