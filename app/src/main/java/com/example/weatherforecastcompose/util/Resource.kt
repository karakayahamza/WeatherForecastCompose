package com.example.weatherforecastcompose.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.example.weatherforecastcompose.R
import rememberIconResource

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

@Composable
fun weatherResources(): WeatherResources {
    return WeatherResources(
        humidityResource = rememberIconResource(
            isSystemInDarkTheme(), R.drawable.humidity_light, R.drawable.humidity_dark
        ),
        windResource = rememberIconResource(
            isSystemInDarkTheme(),
            R.drawable.wind_light,
            R.drawable.wind_dark
        ),
        pressureResource = rememberIconResource(
            isSystemInDarkTheme(),
            R.drawable.pressure_light,
            R.drawable.pressure_dark
        )
    )
}

data class WeatherResources(
    val humidityResource: Painter,
    val windResource: Painter,
    val pressureResource: Painter
)