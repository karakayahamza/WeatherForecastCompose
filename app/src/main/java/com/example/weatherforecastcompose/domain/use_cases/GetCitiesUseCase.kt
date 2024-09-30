package com.example.weatherforecastcompose.domain.use_cases

import com.example.weatherforecastcompose.data.local.CityFromJson
import com.example.weatherforecastcompose.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    fun executeGetCities(): Flow<List<CityFromJson>> = flow {
        val cities = cityRepository.getCities() // Assuming this fetches from JSON
        emit(cities) // Emit the list of cities
    }
}
