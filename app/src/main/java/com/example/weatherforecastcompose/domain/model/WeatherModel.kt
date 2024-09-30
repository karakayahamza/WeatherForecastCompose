package com.example.weatherforecastcompose.domain.model

data class WeatherModel(
    val cityName: String = "",
    val country: String = "",
    val population: Int = 0,
    val sunrise: Int = 0,
    val sunset: Int = 0,
    val weatherDetails: List<WeatherDetails> = emptyList()
)

data class WeatherDetails(
    val dateTime: String = "",
    val feelsLike: Double = 0.0,
    val humidity: Int = 0,
    val pressure: Int = 0,
    val temperature: Double = 0.0,
    val tempMax: Double = 0.0,
    val tempMin: Double = 0.0,
    val windDegree: Int = 0,
    val windSpeed: Double = 0.0,
    val description: String = "",
    val icon: String = ""
)