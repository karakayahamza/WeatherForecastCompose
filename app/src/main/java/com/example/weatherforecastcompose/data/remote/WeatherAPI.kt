package com.example.weatherforecastcompose.data.remote

import com.example.weatherforecastcompose.data.remote.dto.WeatherDto
import com.example.weatherforecastcompose.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

////api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}
//// APIKEY = 61e8b0259c092b1b9a15474cd800ee25


interface WeatherAPI {
    @GET("forecast?&lang=tr")
    suspend fun getData(
        @Query("q") name: String?,
        @Query("APPID") appId: String = API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherDto
}