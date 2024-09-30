package com.example.weatherforecastcompose.domain.repository

import com.example.weatherforecastcompose.data.remote.dto.WeatherDto

interface WeatherRepository {
    suspend fun getWeatherDetails(placeName:String) : WeatherDto
}