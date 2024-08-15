// WeatherViewModel.kt
package com.example.weatherforecastcompose.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.model.CityList
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.repository.CurrentWeatherRepository
import com.example.weatherforecastcompose.repository.WeatherRepository
import com.example.weatherforecastcompose.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val currentRepository: CurrentWeatherRepository
) : ViewModel() {

//    private val _selectedCities = mutableStateListOf<String>()
//    val selectedCities: List<String> get() = _selectedCities

    private val _weatherData = mutableStateMapOf<String, WeatherModel?>()
    val weatherData: Map<String, WeatherModel?> get() = _weatherData

    private val _errorMessages = mutableStateMapOf<String, String>()
    val errorMessages: Map<String, String> get() = _errorMessages

    private val _isLoading = mutableStateMapOf<String, Boolean>()
    val isLoading: Map<String, Boolean> get() = _isLoading

    private val _currentWeatherData = mutableStateOf<WeatherModel?>(null)
    val currentWeatherData: WeatherModel? get() = _currentWeatherData.value

    private val _currentCityName = mutableStateOf<String?>(null)
    val currentCityName: String? get() = _currentCityName.value

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

                else -> {}
            }
            _isLoading[cityName] = false
        }
    }


    fun loadCurrentWeather(lat: String, lon: String) {
        viewModelScope.launch {
            when (val result = currentRepository.getCurrentWeatherList(lat, lon)) {
                is Resource.Success -> {
                    _currentWeatherData.value = result.data
                    // Set city name from the response data
                    _currentCityName.value = result.data?.city?.name
                }

                is Resource.Error -> {
                    println(result.message ?: "Unknown error")
                }

                else -> {
                    println("An error occurred.")
                }
            }
        }
    }

    fun getDistrictNames(context: Context): List<String> {
        val jsonFileString = getJsonDataFromAsset(context, "city_list.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<CityList>>() {}.type
        val cities: List<CityList> = gson.fromJson(jsonFileString, listCityType)

        return cities.flatMap { it.districts }
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}
