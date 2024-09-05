package com.example.weatherforecastcompose.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.model.WeatherModel.WeatherModel
import com.example.weatherforecastcompose.ui.WeatherResources

@Composable
fun WeatherContent(
    currentTemp: Double?,
    forecast: WeatherModel?,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        forecast?.let {
            WeatherMainCard(forecast.city.name, currentTemp, it.list)
            WeatherDetailsCard(
                humidityResource = WeatherResources.getWeatherResources().humidityResource,
                windResource = WeatherResources.getWeatherResources().windResource,
                pressureResource = WeatherResources.getWeatherResources().pressureResource,
                forecast = forecast.list
            )
            SunriseSunsetInfo(it.city.sunrise, it.city.sunset)
            WeatherHourlyDetailsCard(it.list)

            Spacer(modifier = Modifier.height(16.dp))
            TemperatureGraph(it.list.take(5).map { item -> item.main.temp })
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

