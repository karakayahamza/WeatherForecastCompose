package com.example.weatherforecastcompose.repository

import com.example.weatherforecastcompose.model.Root
import com.example.weatherforecastcompose.model.WeatheModel
import com.example.weatherforecastcompose.service.WeatherAPI
import com.example.weatherforecastcompose.util.Constants
import com.example.weatherforecastcompose.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class WeatherRepository@Inject constructor(
    private val api: WeatherAPI
) {

    suspend fun getWeatherList(name:String): Resource<WeatheModel> {
        val response = try {
            api.getData(name,Constants.API_KEY,"metric")
        } catch(e: Exception) {
            println("Hata mesajı: ${e.message}")
            return Resource.Error("Error.")
        }
        return Resource.Success(response)
    }


}