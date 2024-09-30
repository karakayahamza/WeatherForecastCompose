package com.example.weatherforecastcompose.domain.repository

import com.example.weatherforecastcompose.domain.model.UserPreferences

interface PreferencesRepository {
    fun saveUserPreferences(preferences: UserPreferences)
    fun getUserPreferences(): UserPreferences
}
