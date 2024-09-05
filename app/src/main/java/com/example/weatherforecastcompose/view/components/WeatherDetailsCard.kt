package com.example.weatherforecastcompose.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.model.WeatherModel.Root
import com.example.weatherforecastcompose.utils.WeatherUtils

@Composable
fun WeatherDetailsCard(
    humidityResource: Painter,
    windResource: Painter,
    pressureResource: Painter,
    forecast: List<Root>?
) {

    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                WeatherDetailItem(
                    iconResource = humidityResource,
                    value = "${forecast?.get(0)?.main?.humidity}%",
                    label = "Nem",
                    modifier = Modifier.weight(1f)
                )

                WeatherDetailItem(
                    iconResource = windResource,
                    value = "${forecast?.get(0)?.wind?.deg?.let { WeatherUtils.getWindDirection(it) }},${
                        forecast?.get(
                            0
                        )?.wind?.speed?.toInt()
                    } km/h",
                    label = "Rüzgar",
                    modifier = Modifier.weight(1f)
                )

                WeatherDetailItem(
                    iconResource = pressureResource,
                    value = "${forecast?.get(0)?.main?.pressure} hPa",
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
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = iconResource,
            contentDescription = label,
            modifier = Modifier
                .size(35.dp)
                .padding(8.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        infoIcon?.invoke()
    }
}
