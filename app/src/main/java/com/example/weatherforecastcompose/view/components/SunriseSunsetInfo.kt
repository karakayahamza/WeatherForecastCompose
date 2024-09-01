package com.example.weatherforecastcompose.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherforecastcompose.utils.DateUtils.formatTime

@Composable
fun SunriseSunsetInfo(sunrise: Long, sunset: Long) {
    val sunriseTime = formatTime(sunrise)
    val sunsetTime = formatTime(sunset)

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SunriseSunsetColumn(
            label = "Gün Doğumu",
            time = sunriseTime,
            jsonFileName = "sunrise.json"
        )
        Spacer(modifier = Modifier.width(16.dp))
        SunriseSunsetColumn(
            label = "Gün Batımı",
            time = sunsetTime,
            jsonFileName = "sunset.json"
        )
    }
}

@Composable
fun SunriseSunsetColumn(label: String, time: String, jsonFileName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.Asset(jsonFileName)
        )
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .size(55.dp)
        )

        Text(
            text = label,
            fontSize = 12.sp,

            )
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}