package com.example.weatherforecastcompose.data.remote

import com.example.weatherforecastcompose.data.remote.dto.WeatherDto
import com.example.weatherforecastcompose.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("forecast?&lang=tr")
    suspend fun getData(
        @Query("q") name: String?,
        @Query("APPID") appId: String = API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherDto

    @GET("forecast?&lang=tr")
    suspend fun getDataWithLocation(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("APPID") appId: String = API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherDto
}