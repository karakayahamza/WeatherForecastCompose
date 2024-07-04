package com.example.weatherforecastcompose.service

import com.example.weatherforecastcompose.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("forecast?&lang=tr")
    suspend fun getData(
        @Query("q") name: String?,
        @Query("APPID") appId: String?,
        @Query("units") units: String?
    ): WeatherModel
}
