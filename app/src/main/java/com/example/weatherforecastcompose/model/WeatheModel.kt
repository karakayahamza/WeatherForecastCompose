package com.example.weatherforecastcompose.model

data class WeatherModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Root>,
    val message: Int
)