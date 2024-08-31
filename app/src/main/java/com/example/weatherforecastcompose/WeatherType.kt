package com.example.weatherforecastcompose

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
    FOGGY_NIGHT("night_foggy.json")
}