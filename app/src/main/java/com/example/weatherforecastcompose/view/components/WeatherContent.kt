package com.example.weatherforecastcompose.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.ui.weatherResources

@Composable
fun WeatherContent(
    currentTemp: Double?,
    forecast: WeatherModel?,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        forecast?.let {
            WeatherMainCard(forecast.city.name, currentTemp, it.list)
            WeatherDetailsCard(
                humidityResource = weatherResources().humidityResource,
                windResource = weatherResources().windResource,
                pressureResource = weatherResources().pressureResource,
                forecast = forecast.list
            )
            SunriseSunsetInfo(it.city.sunrise, it.city.sunset)
            WeatherHourlyDetailsCard(it.list)

        }
    }
}