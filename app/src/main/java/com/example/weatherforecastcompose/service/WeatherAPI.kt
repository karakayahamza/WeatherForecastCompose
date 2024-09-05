package com.example.weatherforecastcompose.service

import com.example.weatherforecastcompose.model.WeatherModel.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    companion object {
        const val DEFAULT_LANG = "tr"
        const val DEFAULT_UNITS = "metric"
    }

    @GET("forecast")
    suspend fun getDataByCity(
        @Query("q") cityName: String,
        @Query("APPID") appId: String,
        @Query("units") units: String = DEFAULT_UNITS,
        @Query("lang") lang: String = DEFAULT_LANG
    ): WeatherModel

    @GET("forecast")
    suspend fun getDataByCoordinates(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("APPID") appId: String,
        @Query("units") units: String = DEFAULT_UNITS,
        @Query("lang") lang: String = DEFAULT_LANG
    ): WeatherModel
}