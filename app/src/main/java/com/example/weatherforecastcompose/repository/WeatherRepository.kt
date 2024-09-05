package com.example.weatherforecastcompose.repository

import com.example.weatherforecastcompose.BuildConfig
import com.example.weatherforecastcompose.model.WeatherModel.WeatherModel
import com.example.weatherforecastcompose.service.WeatherAPI
import com.example.weatherforecastcompose.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class WeatherRepository @Inject constructor(
    private val api: WeatherAPI
) {
    private val apiKey = BuildConfig.API_KEY
    suspend fun getWeatherListByCity(name: String): Resource<WeatherModel> {
        return try {
            val response = api.getDataByCity(name, apiKey, "metric")
            Resource.Success(response)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Resource.Error("Error.")
        }
    }

    suspend fun getWeatherListByCoordinates(lat: String, lon: String): Resource<WeatherModel> {
        return try {
            val response = api.getDataByCoordinates(lat, lon, apiKey)
            Resource.Success(response)
        } catch (e: Exception) {
            println("Error: ${e.message}")
            Resource.Error("Error.")
        }
    }
}