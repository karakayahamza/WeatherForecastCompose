package com.example.weatherforecastcompose.domain.use_cases

import com.example.weatherforecastcompose.domain.model.UserPreferences
import com.example.weatherforecastcompose.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    fun getUserPreferences(): UserPreferences {
        return preferencesRepository.getUserPreferences()
    }

    fun updateCityPreferences(city: String) {
        val updatedCities =
            preferencesRepository.getUserPreferences().cityNames.toMutableSet().apply {
                add(city)
            }
        preferencesRepository.saveUserPreferences(UserPreferences(cityNames = updatedCities))
    }

    fun updateSelectedCities(selectedCities: Set<String>) {
        preferencesRepository.saveUserPreferences(
            preferencesRepository.getUserPreferences().copy(cityNames = selectedCities)
        )
    }
}