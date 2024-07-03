package com.example.weatherforecastcompose.util

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