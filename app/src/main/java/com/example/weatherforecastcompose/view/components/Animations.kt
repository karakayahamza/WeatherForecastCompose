package com.example.weatherforecastcompose.view.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import com.example.weatherforecastcompose.model.WeatherType

@Composable
fun LottieWeatherAnimationView(weatherCode: String, modifier: Modifier = Modifier) {
    val animationFile = WeatherType.formatWeatherCode(weatherCode)

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile.jsonFile))
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
