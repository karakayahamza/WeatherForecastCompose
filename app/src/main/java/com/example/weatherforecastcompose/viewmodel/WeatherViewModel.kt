package com.example.weatherforecastcompose.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.model.CityList
import com.example.weatherforecastcompose.model.WeatherModel
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
    private val repository: WeatherRepository
) : ViewModel() {

    private var _selectedCities = mutableStateListOf<String>()
    val selectedCities: List<String> get() = _selectedCities

    private val _weatherData = mutableStateMapOf<String, WeatherModel?>()
    val weatherData: Map<String, WeatherModel?> get() = _weatherData

    private val _errorMessages = mutableStateMapOf<String, String>()
    val errorMessages: Map<String, String> get() = _errorMessages

    private val _isLoading = mutableStateMapOf<String, Boolean>()
    val isLoading: Map<String, Boolean> get() = _isLoading

    init {
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
                }
            }
            _isLoading[cityName] = false
        }
    }


    fun getDistrictNames(context: Context): List<String> {
        val jsonFileString = getJsonDataFromAsset(context, "city_list.json")
        val gson = Gson()
        val listCityType = object : TypeToken<List<CityList>>() {}.type
        val cities: List<CityList> = gson.fromJson(jsonFileString, listCityType)

        val districtNames = mutableListOf<String>()
        cities.forEach { city ->
            districtNames.addAll(city.districts)
        }
        return districtNames
    }


    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}