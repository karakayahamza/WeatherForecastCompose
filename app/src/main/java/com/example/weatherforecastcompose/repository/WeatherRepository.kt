package com.example.weatherforecastcompose.repository

import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.service.WeatherAPI
import com.example.weatherforecastcompose.service.WeatherAPICurrentLocation
import com.example.weatherforecastcompose.util.Constants
import com.example.weatherforecastcompose.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class WeatherRepository @Inject constructor(
    private val api: WeatherAPI
) {
    suspend fun getWeatherList(name: String): Resource<WeatherModel> {
        return try {
            val response = api.getData(name, Constants.API_KEY, "metric")
            Resource.Success(response)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Resource.Error("Error.")
        }
    }
}

@ActivityScoped
class CurrentWeatherRepository @Inject constructor(
    private val api: WeatherAPICurrentLocation
) {
    suspend fun getCurrentWeatherList(lat: String, lon: String): Resource<WeatherModel> {
        return try {
            val response = api.getCurrentData(lat, lon, Constants.API_KEY)
            Resource.Success(response)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Resource.Error("Error.")
        }
    }
}
