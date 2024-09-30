package com.example.weatherforecastcompose.util

import android.content.Context
import com.example.weatherforecastcompose.data.local.CityFromJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonParser(private val context: Context) {

    fun loadJsonFromAsset(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    fun parseCities(jsonString: String): List<CityFromJson> {
        val gson = Gson()
        val cityType = object : TypeToken<List<CityFromJson>>() {}.type
        return gson.fromJson(jsonString, cityType)
    }
}
