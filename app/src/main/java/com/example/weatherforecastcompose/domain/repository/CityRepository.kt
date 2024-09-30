package com.example.weatherforecastcompose.domain.repository

import com.example.weatherforecastcompose.data.local.CityFromJson

interface CityRepository {
    fun getCities(): List<CityFromJson>
}
