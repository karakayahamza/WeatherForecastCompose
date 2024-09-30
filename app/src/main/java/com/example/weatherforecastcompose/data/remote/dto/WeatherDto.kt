package com.example.weatherforecastcompose.data.remote.dto

import com.example.weatherforecastcompose.domain.model.WeatherDetails
import com.example.weatherforecastcompose.domain.model.WeatherModel

data class WeatherDto(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item0>,
    val message: Int
)

fun WeatherDto.toWeatherList(): WeatherModel {
     val weatherDetails = list.map { item ->
        WeatherDetails(
            dateTime = item.dt_txt,
            feelsLike = item.main.feels_like,
            humidity = item.main.humidity,
            pressure = item.main.pressure,
            temperature = item.main.temp,
            tempMax = item.main.temp_max,
            tempMin = item.main.temp_min,
            windDegree = item.wind.deg,
            windSpeed = item.wind.speed,
            description = item.weather.firstOrNull()?.description ?: "",
            icon = item.weather.firstOrNull()?.icon ?: ""
        )
    }

    return WeatherModel(
        cityName = city.name,
        country = city.country,
        population = city.population,
        sunrise = city.sunrise,
        sunset = city.sunset,
        weatherDetails = weatherDetails
    )
}

