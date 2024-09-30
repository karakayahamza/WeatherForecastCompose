package com.example.weatherforecastcompose.presentation.weather

sealed class WeatherEvent {
   data class Search(val searchString: String):WeatherEvent()
}