package com.example.weatherforecastcompose.view.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.ui.weatherResources
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle
import ir.ehsannarmani.compose_charts.models.ZeroLineProperties

@Composable
fun WeatherContent(
    currentTemp: Double?,
    forecast: WeatherModel?,
) {
    // Create a scroll state
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),  // Apply vertical scrolling
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        forecast?.let {
            WeatherMainCard(forecast.city.name, currentTemp, it.list)
            WeatherDetailsCard(
                humidityResource = weatherResources().humidityResource,
                windResource = weatherResources().windResource,
                pressureResource = weatherResources().pressureResource,
                forecast = forecast.list
            )
            SunriseSunsetInfo(it.city.sunrise, it.city.sunset)
            WeatherHourlyDetailsCard(it.list)

            // Make sure the chart fits and is scrollable
            Spacer(modifier = Modifier.height(16.dp)) // Add some space before the chart
            TemperatureGraph(it.list.take(9).map { item -> item.main.temp })
            Spacer(modifier = Modifier.height(16.dp)) // Add some space before the chart


        }

    }
}

@Composable
fun TemperatureGraph(values: List<Double>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 22.dp)
    ) {
        LineChart(
            modifier = Modifier.fillMaxSize(),
            data = listOf(
                Line(
                    label = "Temperatures",
                    values = values,
                    color = SolidColor(Color(0xFFFFC107)),
                    firstGradientFillColor = Color(0xFFFFC107).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                )

            ),
            zeroLineProperties = ZeroLineProperties(
                enabled = true,
                color = SolidColor(Color.Red),
            ),
            animationDelay = 300L,

            animationMode = AnimationMode.Together(delayBuilder = {
                it * 5000L
            }),
            gridProperties = GridProperties(
                enabled = false,
                xAxisProperties = GridProperties.AxisProperties(
                    enabled = false,
                    style = StrokeStyle.Dashed(intervals = floatArrayOf(10f, 10f)),
                    thickness = (.5).dp,
                    lineCount = 5
                ),
                yAxisProperties = GridProperties.AxisProperties(enabled = false)
            ),
            labelProperties = LabelProperties(enabled = false),
            labelHelperProperties = LabelHelperProperties(enabled = false),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                count = 5,
                position = IndicatorPosition.Horizontal.Start,
                padding = 5.dp,
                contentBuilder = { indicator ->
                    "%.0f".format(indicator)
                }
            ),
            dividerProperties = DividerProperties(enabled = false),
            popupProperties = PopupProperties(
                enabled = true,
                animationSpec = tween(300),
                duration = 2000L,
                textStyle = MaterialTheme.typography.labelSmall,
                containerColor = Color.White,
                cornerRadius = 8.dp,
                contentHorizontalPadding = 4.dp,
                contentVerticalPadding = 2.dp,
                contentBuilder = { value ->
                    "%.1f".format(value) + "°C"
                }
            )

        )
    }
}
