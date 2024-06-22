package com.example.weatherforecastcompose.service

import com.example.weatherforecastcompose.model.Root
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("forecast?&lang=tr")
    fun getData(
        @Query("q") name: String?,
        @Query("APPID") appId: String?,
        @Query("units") units: String?
    ): Root
}

//api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}

// APIKEY = 61e8b0259c092b1b9a15474cd800ee25
