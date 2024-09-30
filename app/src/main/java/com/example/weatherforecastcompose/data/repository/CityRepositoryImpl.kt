package com.example.weatherforecastcompose.data.repository

import com.example.weatherforecastcompose.data.local.CityFromJson
import com.example.weatherforecastcompose.domain.repository.CityRepository
import com.example.weatherforecastcompose.util.JsonParser
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val jsonParser: JsonParser // Replace with your JSON parsing logic
) : CityRepository {
    override fun getCities(): List<CityFromJson> {
        val jsonString = jsonParser.loadJsonFromAsset("cities.json")
        return jsonParser.parseCities(jsonString) // Implement this method
    }
}