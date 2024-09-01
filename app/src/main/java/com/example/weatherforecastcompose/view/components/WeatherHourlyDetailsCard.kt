package com.example.weatherforecastcompose.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.model.WeatherModel.Root
import com.example.weatherforecastcompose.util.DateUtils.formatDate

@Composable
fun WeatherHourlyDetailsCard(forecast: List<Root>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(forecast.slice(1..5)) { weather ->
            val (date) = formatDate(weather.dt_txt)
            ExpandableCard(date = date, weather = weather)
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}