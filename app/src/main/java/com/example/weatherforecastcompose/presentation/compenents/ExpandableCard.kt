package com.example.weatherforecastcompose.presentation.compenents

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.domain.model.WeatherDetails

@Composable
fun ExpandableCard(
    date: String, weather: WeatherDetails
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 45f else -45f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { expanded = !expanded }
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = date, fontSize = 14.sp, textAlign = TextAlign.Start
                )
                LottieWeatherAnimationView(
                    weather.icon, Modifier.size(50.dp)
                )

                Text(
                    text = "${weather.temperature.toInt()}°C",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                        .align(Alignment.End)
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = "Hissedilen: ${weather.feelsLike.toInt()}°C", fontSize = 10.sp
                    )

                    Text(
                        text = "Nem: ${weather.humidity} %", fontSize = 10.sp
                    )

                    Text(
                        text = "Basınç: ${weather.pressure} hPa", fontSize = 10.sp
                    )

                    Text(
                        text = "Rüzgar Hızı: ${weather.windSpeed.toInt()} km/h", fontSize = 10.sp
                    )
                }
            }
        }
    }
}