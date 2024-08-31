package com.example.weatherforecastcompose.view.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherforecastcompose.WeatherType

@Composable
fun LottieWeatherAnimationView(weatherCode: String, modifier: Modifier = Modifier) {
    val animationFile = mapWeatherCodeToJsonFile(weatherCode)

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

fun mapWeatherCodeToJsonFile(weatherCode: String): String {
    return when (weatherCode) {
        "01d" -> WeatherType.CLEAR_DAY.jsonFile
        "01n" -> WeatherType.CLEAR_NIGHT.jsonFile
        "02d" -> WeatherType.PARTLY_CLOUDY_DAY.jsonFile
        "02n" -> WeatherType.PARTLY_CLOUDY_NIGHT.jsonFile
        "03d", "03n", "04d", "04n" -> WeatherType.CLOUDY_NIGHT.jsonFile
        "09d", "10d" -> WeatherType.RAINY_DAY.jsonFile
        "09n", "10n" -> WeatherType.RAINY_NIGHT.jsonFile
        "11d" -> WeatherType.THUNDER_DAY.jsonFile
        "11n" -> WeatherType.THUNDER_NIGHT.jsonFile
        "13d" -> WeatherType.SNOWY_DAY.jsonFile
        "13n" -> WeatherType.SNOWY_NIGHT.jsonFile
        "50d" -> WeatherType.FOGGY_DAY.jsonFile
        "50n" -> WeatherType.FOGGY_NIGHT.jsonFile
        else -> WeatherType.CLEAR_DAY.jsonFile
    }
}

@Composable
fun AnimatedNavDrawerMenuButton(isOpen: Boolean, onToggle: () -> Unit) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isOpen) 90f else 0f,
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isOpen) Icons.Default.Close else Icons.Default.Menu,
            contentDescription = if (isOpen) "Close menu" else "Open menu",
            modifier = Modifier.graphicsLayer(rotationZ = rotationAngle)
        )
    }
}
