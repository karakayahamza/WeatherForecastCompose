package com.example.weatherforecastcompose.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.weatherforecastcompose.R
import javax.annotation.concurrent.Immutable

@Immutable
data class WeatherResources(
    val humidityResource: Painter,
    val windResource: Painter,
    val pressureResource: Painter
) {

    companion object {
        @Composable
        fun getWeatherResources(): WeatherResources {
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
    }
}