package com.example.weatherforecastcompose.data.local

import android.content.SharedPreferences
import com.example.weatherforecastcompose.domain.model.UserPreferences

class SharedPreferencesDataSource(private val sharedPreferences: SharedPreferences) {

    private fun saveCityNames(cityNames: Set<String>) {
        sharedPreferences.edit().putStringSet("cityNames", cityNames).apply()
    }

    private fun getCityNames(): Set<String>? {
        return sharedPreferences.getStringSet("cityNames", null)
    }

    fun getUserPreferences(): UserPreferences {
        val cityNames = getCityNames() ?: emptySet()
        return UserPreferences(cityNames)
    }

    fun saveUserPreferences(preferences: UserPreferences) {
        saveCityNames(preferences.cityNames)
    }
}
