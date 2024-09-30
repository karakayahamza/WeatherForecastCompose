package com.example.weatherforecastcompose.presentation.compenents

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherforecastcompose.domain.model.WeatherModel
import com.example.weatherforecastcompose.presentation.weather.WeatherState
import com.example.weatherforecastcompose.util.WeatherResources
import com.example.weatherforecastcompose.util.WeatherUtils

@Composable
fun WeatherPage(weatherState: WeatherState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WeatherMainCard(
            city = weatherState.weather.cityName, currentTemp = 1.2, weatherState = weatherState
        )
        Spacer(modifier = Modifier.height(16.dp))
        WeatherDetailsCard(
            humidityResource = WeatherResources.getWeatherResources().humidityResource,
            windResource = WeatherResources.getWeatherResources().windResource,
            pressureResource = WeatherResources.getWeatherResources().pressureResource,
            weatherMode = weatherState.weather
        )
        Spacer(modifier = Modifier.height(16.dp))
        SunriseSunsetInfo(
            weatherState.weather.sunrise.toLong(),
            weatherState.weather.sunset.toLong()
        )
        Spacer(modifier = Modifier.height(16.dp))
        WeatherHourlyDetailsCard(weatherState.weather)
    }
}

@Composable
fun WeatherMainCard(
    city: String, currentTemp: Double?, weatherState: WeatherState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = city,
                fontSize = 30.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = WeatherUtils.formatDate(
                    weatherState.weather.weatherDetails[0].dateTime ?: "-"
                ).first,
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
                LottieWeatherAnimationView(
                    weatherState.weather.weatherDetails[0].icon, Modifier.size(78.dp)
                )

                Text(
                    text = weatherState.weather.weatherDetails[0].description.uppercase(),
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }

            currentTemp?.let { TemperatureInfo(currentTemp) }


            Text(
                text = "Hissedilen: ${weatherState.weather.weatherDetails.get(0)?.feelsLike} °C",
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


@Composable
fun WeatherDetailsCard(
    humidityResource: Painter,
    windResource: Painter,
    pressureResource: Painter,
    weatherMode: WeatherModel
) {

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                WeatherDetailItem(
                    iconResource = humidityResource,
                    value = "${weatherMode.weatherDetails[0].humidity}%",
                    label = "Nem",
                    modifier = Modifier.weight(1f)
                )


                WeatherDetailItem(iconResource = windResource, value = "${
                    weatherMode.weatherDetails[0].windDegree.let {
                        WeatherUtils.getWindDirection(
                            it
                        )
                    }
                },${
                    weatherMode.weatherDetails[0].windSpeed.toInt()
                } km/h", label = "Rüzgar", modifier = Modifier.weight(1f))

                WeatherDetailItem(
                    iconResource = pressureResource,
                    value = "${weatherMode.weatherDetails[0].pressure} hPa",
                    label = "Basınç",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WeatherDetailItem(
    iconResource: Painter,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    infoIcon: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = iconResource,
            contentDescription = label,
            modifier = Modifier
                .size(35.dp)
                .padding(8.dp)
        )
        Text(
            text = value, fontSize = 14.sp, textAlign = TextAlign.Center
        )
        Text(
            text = label, fontSize = 10.sp, textAlign = TextAlign.Center
        )
        infoIcon?.invoke()
    }
}


@Composable
fun SunriseSunsetInfo(sunrise: Long, sunset: Long) {
    val sunriseTime = WeatherUtils.formatTime(sunrise)
    val sunsetTime = WeatherUtils.formatTime(sunset)

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