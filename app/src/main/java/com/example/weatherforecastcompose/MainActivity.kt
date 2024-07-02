package com.example.weatherforecastcompose

import MainScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.WeatherForecastComposeTheme
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: WeatherViewModel = hiltViewModel()
            WeatherForecastComposeTheme {
                MainScreen(viewModel)
            }

            //VibrantAnimation()
        }
    }


}

enum class WeatherType {
    Clear, // Açık hava durumu
    Cloudy, // Bulutlu hava durumu
    Rainy, // Yağmurlu hava durumu
    // diğer hava durumu türleri buraya eklenebilir
}

@Composable
fun VibrantAnimation(weatherType: WeatherType) {
    var targetColor by remember { mutableStateOf(getColorForWeather(weatherType)) }

    LaunchedEffect(weatherType) {
        targetColor = getColorForWeather(weatherType)
    }

    val transition = rememberInfiniteTransition()

    val color by transition.animateColor(
        initialValue = targetColor.copy(alpha = 0.5f), // Başlangıçta biraz saydam
        targetValue = targetColor.copy(alpha = 1f), // Tam opaklıkta hedef renk
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Ölçeklendirme animasyonu
    val scale by transition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                0.5f at 2000
                1.0f at 4000
            },
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .size(300.dp * scale)
    )
}

// Hava durumu türüne göre renk seçimi
fun getColorForWeather(weatherType: WeatherType): Color {
    return when (weatherType) {
        WeatherType.Clear -> Color.Blue // Örneğin, açık hava durumu için mavi renk
        WeatherType.Cloudy -> Color.Gray // Bulutlu hava durumu için gri renk
        WeatherType.Rainy -> Color.DarkGray // Yağmurlu hava durumu için koyu gri renk
        // diğer hava durumu türleri buraya göre renkler atanabilir
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVibrantAnimation() {
    VibrantAnimation(WeatherType.Rainy)
}

