package com.example.weatherforecastcompose.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.model.Root

@Composable
fun ExpandableCard(
    date: String, dayOfWeek: String, weather: Root
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(if (expanded) 45f else -45f, label = "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 4.dp)
            .animateContentSize()
            .border(
                shape = RoundedCornerShape(2),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
            )
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded = !expanded }
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = date, fontSize = 14.sp, textAlign = TextAlign.Start
                    )
                    LottieWeatherAnimationView(
                        weather.weather[0].icon, Modifier.size(50.dp)
                    )
                    Text(
                        text = "${weather.main.temp.toInt()}°C",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )

                    Row(modifier = Modifier.align(alignment = Alignment.End)) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(rotationAngle)
                                .align(Alignment.Bottom)
                        )
                    }
                }

                if (expanded) {
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = "Hissedilen: ${weather.main.feels_like.toInt()}°C",
                            fontSize = 10.sp
                        )

                        Text(
                            text = "Nem: ${weather.main.humidity} %",
                            fontSize = 10.sp
                        )

                        Text(
                            text = "Basınç: ${weather.main.pressure} hPa",
                            fontSize = 10.sp
                        )

                        Text(
                            text = "Rüzgar Hızı: ${weather.wind.speed.toInt()} km/h",
                            fontSize = 10.sp
                        )

                    }
                }

            }
        }
    }
}


