package com.example.weatherforecastcompose.viewmodel

import androidx.compose.runtime.State
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
    private val repository: WeatherRepository
) : ViewModel() {

    private var _selectedCities = mutableStateListOf<String>()
    val selectedCities: List<String> get() = _selectedCities

    private var _weather = mutableStateOf<WeatheModel?>(null)
    val weather: State<WeatheModel?> get() = _weather

    private var _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    init {
        // Default bir şehir yükleyebilirsiniz (örneğin ilk seçili şehir)
        _selectedCities.add("DefaultCityName")
        loadWeather(_selectedCities.first())
    }

    fun loadWeather(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getWeatherList(cityName)) {
                is Resource.Success -> {
                    _weather.value = result.data
                    _errorMessage.value = ""
                }
                is Resource.Error -> {
                    _errorMessage.value = result.message ?: "Unknown error"
                }
                is Resource.Loading -> {
                    // Burada gerekirse Loading durumuyla ilgili bir şey yapılabilir
                }
            }
            _isLoading.value = false
        }
    }

    fun addCity(city: String) {
        if (!_selectedCities.contains(city)) {
            _selectedCities.add(city)
        }
    }

    fun removeCity(city: String) {
        _selectedCities.remove(city)
    }
}
