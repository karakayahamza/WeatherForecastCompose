package com.example.weatherforecastcompose.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveSelectedCities(selectedCities: List<String>, context: Context) {
    val json = Gson().toJson(selectedCities)
    val sharedPreferences = context.getSharedPreferences("selected_cities", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("cities", json)
    editor.apply()
}

fun loadSelectedCities(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences("selected_cities", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("cities", null)
    return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: emptyList()

}