package com.example.weatherforecastcompose.utils
import android.location.Location

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
