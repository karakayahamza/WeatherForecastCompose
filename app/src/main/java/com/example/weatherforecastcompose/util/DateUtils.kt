package com.example.weatherforecastcompose.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatTime(sunriseOrSunsetTimestamp: Long): String {
        val date = Date(sunriseOrSunsetTimestamp * 1000)
        val convetData = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return convetData.format(date)
    }

    @SuppressLint("DefaultLocale")
    fun formatDate(dateString: String): Pair<String, String> {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date = LocalDateTime.parse(dateString, inputFormatter)
        //val day = date.dayOfMonth
        //val month = date.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("tr"))
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale("tr"))
        val time =
            "${date.hour}:${String.format("%02d", date.minute)}"

        return Pair(time, dayOfWeek)
    }
}
