package com.example.weatherforecastcompose.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
    private val repository: WeatherRepository
) : ViewModel() {

    private var _selectedCities = mutableStateListOf<String>()
    val selectedCities: List<String> get() = _selectedCities

    private val _weatherData = mutableStateMapOf<String, WeatheModel?>()
    val weatherData: Map<String, WeatheModel?> get() = _weatherData

    private val _errorMessages = mutableStateMapOf<String, String>()
    val errorMessages: Map<String, String> get() = _errorMessages

    private val _isLoading = mutableStateMapOf<String, Boolean>()
    val isLoading: Map<String, Boolean> get() = _isLoading

    init {
        // Default bir şehir yükleyebilirsiniz (örneğin ilk seçili şehir)
        _selectedCities.add("DefaultCityName")
        loadWeather(_selectedCities.first())
    }

    fun loadWeather(cityName: String) {
        viewModelScope.launch {
            _isLoading[cityName] = true
            when (val result = repository.getWeatherList(cityName)) {
                is Resource.Success -> {
                    _weatherData[cityName] = result.data
                    _errorMessages[cityName] = ""
                }
                is Resource.Error -> {
                    _errorMessages[cityName] = result.message ?: "Unknown error"
                }
                is Resource.Loading -> {
                    // Burada gerekirse Loading durumuyla ilgili bir şey yapılabilir
                }
            }
            _isLoading[cityName] = false
        }
    }

    fun addCity(city: String) {
        if (!_selectedCities.contains(city)) {
            _selectedCities.add(city)
        }
    }

    fun removeCity(city: String) {
        _selectedCities.remove(city)
        _weatherData.remove(city)
        _errorMessages.remove(city)
        _isLoading.remove(city)
    }
}

