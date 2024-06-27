package com.example.weatherforecastcompose.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.model.Root
import com.example.weatherforecastcompose.model.WeatheModel
import com.example.weatherforecastcompose.repository.WeatherRepository
import com.example.weatherforecastcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var selectedCities = mutableStateListOf<String>()
        private set

    var weather = mutableStateOf<WeatheModel?>(null)
        private set

    var errorMessage = mutableStateOf("")
        private set

    var isLoading = mutableStateOf(false)
        private set

    init {
        val name: String = savedStateHandle["name"] ?: "DefaultCityName"
        loadWeather(name)
    }

    fun loadWeather(name: String) {
        println("Name: $name")
        viewModelScope.launch {
            isLoading.value = true
            when(val result = repository.getWeatherList(name)) {
                is Resource.Success -> {
                    val weatherData = result.data
                    if (weatherData != null) {
                        weather.value = weatherData
                    }
                    errorMessage.value = ""
                    isLoading.value = false
                }
                is Resource.Error -> {
                    errorMessage.value = result.message ?: "Unknown error"
                    isLoading.value = false
                }
                is Resource.Loading -> {
                    errorMessage.value = "Loading"
                }
            }
        }
    }

    fun addCity(city: String) {
        if (!selectedCities.contains(city)) {
            selectedCities.add(city)
        }
    }

    fun removeCity(city: String) {
        selectedCities.remove(city)
    }
}
