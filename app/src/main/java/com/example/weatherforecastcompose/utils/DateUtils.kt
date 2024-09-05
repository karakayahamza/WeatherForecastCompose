package com.example.weatherforecastcompose.utils

import android.annotation.SuppressLint
import android.location.Location
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

class WeatherUtils {
    companion object {
        fun formatTime(sunriseOrSunsetTimestamp: Long): String {
            val date = Date(sunriseOrSunsetTimestamp * 1000)
            val convetData = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return convetData.format(date)
        }

        @SuppressLint("DefaultLocale")
        fun formatDate(dateString: String): Pair<String, String> {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val date = LocalDateTime.parse(dateString, inputFormatter)
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale("tr"))
            val time = "${date.hour}:${String.format("%02d", date.minute)}"

            return Pair(time, dayOfWeek)
        }

        fun distanceBetween(start: Pair<Double, Double>, end: Pair<Double, Double>): Float {
            val startLocation = Location("").apply {
                latitude = start.first
                longitude = start.second
            }
            val endLocation = Location("").apply {
                latitude = end.first
                longitude = end.second
            }
            return startLocation.distanceTo(endLocation)
        }

        fun getWindDirection(degrees: Int): String {
            return when (degrees) {
                in 0..22 -> "K"
                in 23..67 -> "KD"
                in 68..112 -> "D"
                in 113..157 -> "GD"
                in 158..202 -> "G"
                in 203..247 -> "GB"
                in 248..292 -> "B"
                in 293..337 -> "KB"
                in 338..360 -> "K"
                else -> "Bilinmiyor"
            }
        }
    }
}
