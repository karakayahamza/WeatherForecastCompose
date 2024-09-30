package com.example.weatherforecastcompose.domain.model

data class UserPreferences(
    val cityNames: Set<String> = emptySet()
)
