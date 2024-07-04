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

@Composable
fun LottieWeatherAnimationView(animationCode: String,modifier: Modifier) {
    val animationFile = when (animationCode) {
        "01d" -> "day_clear.json"
        "01n" -> "night_clear.json"
        "02d" -> "day_partly_cloudy.json"
        "02n" -> "night_partly_cloudly.json"
        "03d" -> "night_cloudly.json"
        "03n" -> "night_cloudly.json"
        "04d" -> "night_cloudly.json"
        "04n" -> "night_cloudly.json"
        "09d" -> "day_rainy.json"
        "09n" -> "night_rainy.json"
        "10d" -> "day_rainy.json"
        "10n" -> "night_rainy.json"
        "11d" -> "day_thunder_rainy.json"
        "11n" -> "night_thunder_rainy.json"
        "13d" -> "day_snowy.json"
        "13n" -> "night_snowy.json"
        "50d" -> "day_foggy.json"
        "50n" -> "night_foggy.json"
        else -> "day_clear.json"
    }

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

@Composable
fun AnimatedNavDrawerMenuButton(isOpen: Boolean, onToggle: () -> Unit) {
    val transitionSpec = tween<Float>(durationMillis = 100, easing = LinearOutSlowInEasing)

    val rotationAngle by animateFloatAsState(
        targetValue = if (isOpen) 90f else 0f,
        animationSpec = transitionSpec, label = ""
    )

    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isOpen) Icons.Default.Close else Icons.Default.Menu,
            contentDescription = if (isOpen) "Close menu" else "Open menu",
            modifier = Modifier.graphicsLayer(rotationZ = rotationAngle)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}
