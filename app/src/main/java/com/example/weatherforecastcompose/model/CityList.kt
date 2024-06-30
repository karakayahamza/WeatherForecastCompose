package com.example.weatherforecastcompose.model

import com.google.gson.annotations.SerializedName

data class CityList(
    @SerializedName("city") val cityName: String,
    @SerializedName("districts") val districts: List<String>
)