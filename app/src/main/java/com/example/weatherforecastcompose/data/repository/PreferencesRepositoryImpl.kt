package com.example.weatherforecastcompose.data.repository

import com.example.weatherforecastcompose.data.local.SharedPreferencesDataSource
import com.example.weatherforecastcompose.domain.model.UserPreferences
import com.example.weatherforecastcompose.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataSource: SharedPreferencesDataSource
) : PreferencesRepository {

    override fun saveUserPreferences(preferences: UserPreferences) {
        dataSource.saveUserPreferences(preferences)
    }

    override fun getUserPreferences(): UserPreferences {
        return dataSource.getUserPreferences()
    }
}
