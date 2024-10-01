package com.example.weatherforecastcompose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.weatherforecastcompose.presentation.ui.theme.WeatherForecastComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecastComposeTheme {
                SplashScreenContent {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}


@Composable
fun SplashScreenContent(onSplashFinish: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onSplashFinish()
    }

    val colorScheme = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.primary
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val imageSize = min(screenWidth, screenHeight) * 0.5f

            // Animated alpha for fade-in
            val alpha = remember { Animatable(0f) }

            // Start animation when the composable is launched
            LaunchedEffect(Unit) {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 3000)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(imageSize)
                        .alpha(alpha.value)
                )
            }
        }
    }
}
