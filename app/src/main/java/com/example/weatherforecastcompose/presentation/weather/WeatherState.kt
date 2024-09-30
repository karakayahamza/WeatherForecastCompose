package com.example.weatherforecastcompose.presentation.weather

import com.example.weatherforecastcompose.domain.model.WeatherModel

data class WeatherState(
    val isLoading: Boolean = true,
    val weather: WeatherModel = WeatherModel(),
    val error: String = "",
    val search: String = ""
)