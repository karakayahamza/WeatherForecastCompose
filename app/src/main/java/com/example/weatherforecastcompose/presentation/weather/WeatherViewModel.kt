package com.example.weatherforecastcompose.presentation.weather

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastcompose.data.local.CityFromJson
import com.example.weatherforecastcompose.domain.model.WeatherModel
import com.example.weatherforecastcompose.domain.repository.PreferencesRepository
import com.example.weatherforecastcompose.domain.use_cases.GetCitiesUseCase
import com.example.weatherforecastcompose.domain.use_cases.GetWeatherUseCase
import com.example.weatherforecastcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val getCitiesUseCase: GetCitiesUseCase
) : ViewModel() {

    private val _weatherStates = mutableStateMapOf<String, WeatherState>()
    val weatherStates: Map<String, WeatherState> = _weatherStates

    private var _cities = mutableStateListOf<String>()
    val cities: List<String> get() = _cities

    private val _cityNames = MutableStateFlow<List<CityFromJson>>(emptyList())
    val cityNames: StateFlow<List<CityFromJson>> = _cityNames

    private val _selectedCities = MutableStateFlow<Set<String>>(emptySet())
    val selectedCities: StateFlow<Set<String>> = _selectedCities

    private val jobQueue = Channel<String>(Channel.UNLIMITED)
    private var currentJob: Job? = null

    init {
        fetchCities()
        loadUserPreferences()
        processQueue()
    }

    private fun processQueue() {
        viewModelScope.launch {
            for (placeName in jobQueue) {
                currentJob?.cancel()

                currentJob = launch {
                    getWeatherDetail(placeName)
                }
                currentJob?.join()
            }
        }
    }

    private fun getWeatherDetail(placeName: String) {
        if (_weatherStates.containsKey(placeName)) return

        getWeatherUseCase.executeGetWeatherDetail(placeName = placeName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _weatherStates[placeName] = WeatherState(
                        weather = result.data ?: WeatherModel(),
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    _weatherStates[placeName] = WeatherState(
                        error = result.message ?: "Error fetching weather",
                        isLoading = false
                    )
                }

                is Resource.Loading -> {
                    _weatherStates[placeName] = WeatherState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadUserPreferences() {
        val currentPreferences = preferencesRepository.getUserPreferences()
        _selectedCities.value = currentPreferences.cityNames
        currentPreferences.cityNames.forEach { city ->
            addCity(city)
        }
    }

    fun addCity(city: String) {
        if (_weatherStates.containsKey(city)) return

        val currentPreferences = preferencesRepository.getUserPreferences()
        val updatedCities = currentPreferences.cityNames.toMutableSet().apply {
            add(city)
        }
        preferencesRepository.saveUserPreferences(currentPreferences.copy(cityNames = updatedCities))

        _cities.add(city)

        jobQueue.trySend(city).isSuccess
    }

    fun toggleCitySelection(city: String, isSelected: Boolean) {
        val updatedSelectedCities = _selectedCities.value.toMutableSet()
        if (isSelected) {
            updatedSelectedCities.add(city)
        } else {
            updatedSelectedCities.remove(city)
        }
        _selectedCities.value = updatedSelectedCities
        preferencesRepository.saveUserPreferences(preferencesRepository.getUserPreferences().copy(cityNames = updatedSelectedCities))
    }

    private fun fetchCities() {
        viewModelScope.launch {
            getCitiesUseCase.executeGetCities().collect { cityList ->
                _cityNames.value = cityList
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}
