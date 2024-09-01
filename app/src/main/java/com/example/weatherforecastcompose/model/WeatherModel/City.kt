package com.example.weatherforecastcompose.model.WeatherModel

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Int
)