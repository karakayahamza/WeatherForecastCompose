package com.example.weatherforecastcompose.data.repository

import com.example.weatherforecastcompose.data.remote.WeatherAPI
import com.example.weatherforecastcompose.data.remote.dto.WeatherDto
import com.example.weatherforecastcompose.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherAPI) : WeatherRepository {
    override suspend fun getWeatherDetails(placeName: String): WeatherDto {
        return api.getData(placeName)
    }

    override suspend fun getWeatherDetailsWithLocation(lat: Double, lon: Double): WeatherDto {
        return api.getDataWithLocation(lat, lon)
    }
}