package com.example.weatherforecastcompose.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.model.WeatherModel.Root
import com.example.weatherforecastcompose.utils.WeatherUtils

@Composable
fun WeatherMainCard(
    city: String, currentTemp: Double?, forecast: List<Root>?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = city,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = WeatherUtils.formatDate(forecast?.get(0)?.dt_txt ?: "-").first,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                forecast?.get(0)?.weather?.get(0)?.icon?.let {
                    LottieWeatherAnimationView(it, Modifier.size(78.dp))
                }

                forecast?.get(0)?.weather?.get(0)?.description?.let { description ->
                    Text(
                        text = description.uppercase(),
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }
            }

            currentTemp?.let { TemperatureInfo(currentTemp) }


            Text(
                text = "Hissedilen: ${forecast?.get(0)?.main?.feels_like} °C",
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TemperatureInfo(currentTemp: Double) {
    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = String.format("%.1f", currentTemp),
            fontSize = 45.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "°C",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.Top)
                .padding(top = 4.dp),
            textAlign = TextAlign.Start
        )
    }
}