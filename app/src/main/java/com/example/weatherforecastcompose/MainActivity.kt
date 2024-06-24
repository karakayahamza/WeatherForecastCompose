package com.example.weatherforecastcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.weatherforecastcompose.model.Root
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecastComposeTheme {
                MainScreen(
                    modifier = Modifier.padding()
                )
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "crypto_list") {
        composable(
            "crypto_list",
            arguments = listOf(navArgument("name") {
                type = NavType.StringType; defaultValue = "Izmir"
            })
        ) { backStackEntry ->
            CryptoList(
                viewModel = hiltViewModel(
                    viewModelStoreOwner = backStackEntry
                )
            )
        }
    }
}

@Composable
fun CryptoList(viewModel: WeatherViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.loadWeather("Izmir")
    }
    val cryptoList by remember { viewModel.weather }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    if (isLoading) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }

    if (errorMessage.isNotEmpty()) {
        println("Error: $errorMessage")
        RetryView(error = errorMessage) {
            viewModel.loadWeather(name = "Izmir")
        }
    } else {
        cryptoList?.city?.name?.let { cityName ->
            cryptoList?.list?.get(0)?.main?.temp?.let { currentTemp ->
                WeatherUI(cityName, currentTemp, cryptoList?.list)
            }
        }
    }
}

@Composable
fun RetryView(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(error, color = Color.Red, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

fun formatDate(dateString: String): Pair<String, String> {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val date = LocalDateTime.parse(dateString, inputFormatter)

    val day = date.dayOfMonth
    val month = date.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("tr"))
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale("tr"))
    val time =
        "${date.hour}:${String.format("%02d", date.minute)}" // Format minutes with leading zero

    val formattedDate = "$day $month"
    return Pair("$formattedDate $time", dayOfWeek)
}

@Composable
fun WeatherUI(city: String, currentTemp: Double?, forecast: List<Root>?) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(state),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val painter =
            rememberImagePainter(
                "https://openweathermap.org/img/wn/${
                    forecast?.get(0)?.weather?.get(
                        0
                    )?.icon
                }@2x.png"
            )

        Text(
            text = city,
            fontSize = 45.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .shadow(40.dp, CircleShape, false)
        ) {
            Image(
                painter = painter,
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .fillMaxSize()
            )
        }


        // Current temperature and city name
        Text(
            text = "$currentTemp°C",
            fontSize = 32.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Scrollable temperature values
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            forecast?.forEach { hourlyForecast ->
                TemperatureCard(hourlyForecast)
            }
        }
    }
}

@Composable
fun TemperatureCard(forecast: Root) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp) // Adjusted width for better layout
            .height(180.dp) // Fixed height for all cards
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter =
                rememberImagePainter("https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png")
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .shadow(25.dp, CircleShape, false)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display temperature
            Text(
                text = "${forecast.main.temp}°C",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Format the date and day of the week
            val (formattedDate, dayOfWeek) = formatDate(forecast.dt_txt)

            // Display the day of the week
            Text(
                text = dayOfWeek,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Display the formatted date
            Text(
                text = formattedDate,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherForecastComposeTheme {
        MainScreen(modifier = Modifier.padding())
    }
}
