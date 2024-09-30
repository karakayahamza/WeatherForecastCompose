package com.example.weatherforecastcompose.data.local

enum class WeatherType(val jsonFile: String) {
    CLEAR_DAY("day_clear.json"),
    CLEAR_NIGHT("night_clear.json"),
    PARTLY_CLOUDY_DAY("day_partly_cloudy.json"),
    PARTLY_CLOUDY_NIGHT("night_partly_cloudy.json"),
    CLOUDY_NIGHT("night_cloudy.json"),
    RAINY_DAY("day_rainy.json"),
    RAINY_NIGHT("night_rainy.json"),
    THUNDER_DAY("day_thunder_rainy.json"),
    THUNDER_NIGHT("night_thunder_rainy.json"),
    SNOWY_DAY("day_snowy.json"),
    SNOWY_NIGHT("night_snowy.json"),
    FOGGY_DAY("day_foggy.json"),
    FOGGY_NIGHT("night_foggy.json");

    companion object {

        fun formatWeatherCode(weatherCode: String): WeatherType {
            return when (weatherCode) {
                "01d" -> CLEAR_DAY
                "01n" -> CLEAR_NIGHT
                "02d" -> PARTLY_CLOUDY_DAY
                "02n" -> PARTLY_CLOUDY_NIGHT
                "03d", "03n", "04d", "04n" -> CLOUDY_NIGHT
                "09d", "10d" -> RAINY_DAY
                "09n", "10n" -> RAINY_NIGHT
                "11d" -> THUNDER_DAY
                "11n" -> THUNDER_NIGHT
                "13d" -> SNOWY_DAY
                "13n" -> SNOWY_NIGHT
                "50d" -> FOGGY_DAY
                "50n" -> FOGGY_NIGHT
                else -> CLEAR_DAY
            }

        }

    }

}