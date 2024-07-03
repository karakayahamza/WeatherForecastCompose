package com.example.weatherforecastcompose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.weatherforecastcompose.R

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

@Composable
fun rememberIconResource(isDarkTheme: Boolean, lightIcon: Int, darkIcon: Int): Painter {
    return if (isDarkTheme) {
        painterResource(id = lightIcon)
    } else {
        painterResource(id = darkIcon)
    }
}

@Immutable
data class WeatherResources(
    val humidityResource: Painter,
    val windResource: Painter,
    val pressureResource: Painter
)
