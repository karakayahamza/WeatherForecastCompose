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
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.ui.weatherResources

@Composable
fun WeatherContent(
    currentTemp: Double?,
    forecast: WeatherModel?,
) {
    // Create a scroll state
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),  // Apply vertical scrolling
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

            // Make sure the chart fits and is scrollable
            Spacer(modifier = Modifier.height(16.dp)) // Add some space before the chart
            TemperatureGraph(it.list.take(5).map { item -> item.main.temp })
            Spacer(modifier = Modifier.height(16.dp)) // Add some space before the chart

        }
    }
}

