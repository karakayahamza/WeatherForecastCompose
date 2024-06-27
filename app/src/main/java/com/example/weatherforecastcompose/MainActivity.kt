package com.example.weatherforecastcompose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
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
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.math.absoluteValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecastComposeTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var searchQuery by remember { mutableStateOf("") }
                val cityNames = getDistrictNames(this@MainActivity)
                val filteredCityNames = remember(searchQuery) {
                    cityNames.filter { it.cityName.contains(searchQuery, ignoreCase = true) }
                }

                val selectedCities = remember { mutableStateListOf<String>() }

                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet(
                            Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(),
                            drawerContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search cities") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyColumn {
                                items(filteredCityNames) { city ->
                                    CityCheckbox(city, selectedCities)
                                    Divider(color = Color.Gray)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }, drawerState = drawerState
                ) {
                    Scaffold(topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }, modifier = Modifier.align(Alignment.TopStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }
                    }, content = {
                        MainScreen(selectedCities)
                    })
                }
            }
        }
    }
}

@Composable
fun MainScreen(selectedCities: List<String>) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "weather_list") {
        composable(
            "weather_list", arguments = listOf(navArgument("name") {
                type = NavType.StringType; defaultValue = "Izmir"
            })
        ) { backStackEntry ->
            WeatherList(
                viewModel = hiltViewModel(
                    viewModelStoreOwner = backStackEntry
                ), selectedCities
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherList(viewModel: WeatherViewModel = hiltViewModel(), selectedCities: List<String>) {
    val pagerState = rememberPagerState(pageCount = { selectedCities.size })

    // Seçili şehirlerin verilerini saklamak için bir map kullanacağız
    val cityWeatherMap = remember { mutableMapOf<String, Root>() }

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        val cityName = selectedCities[page]

        // Şehir daha önce yüklenmemişse veya güncel veri yoksa, veriyi yükle
        if (!cityWeatherMap.containsKey(cityName) || cityWeatherMap[cityName] == null) {
            LaunchedEffect(cityName) {
                viewModel.loadWeather(cityName)
            }
        }

        // View Model'den verileri al
        val weather = viewModel.weather.value
        val errorMessage = viewModel.errorMessage.value
        val isLoading = viewModel.isLoading.value

        // Hata durumunda hata mesajını göster ve yeniden yükleme butonu sağla
        if (errorMessage.isNotEmpty()) {
            RetryView(error = errorMessage) {
                viewModel.loadWeather(cityName)
            }
        } else {
            // Veri yüklendiyse, hava durumu arayüzünü göster
            weather?.let {
                cityWeatherMap[cityName] = it.list[0] // Veriyi map'e kaydet
                val currentWeather = it.list.firstOrNull()?.main
                if (currentWeather != null) {
                    WeatherUI(cityName, currentWeather.temp, it.list)
                }
            }
        }
    }
}



//WeatherUI(city, currentTemp, weather?.list)
@Composable
fun RetryView(
    error: String, onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(error, color = Color.Red, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onRetry() }, modifier = Modifier.align(Alignment.CenterHorizontally)
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

        val painter = rememberImagePainter(
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
                modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.fillMaxSize(),
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
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display temperature
            Text(
                text = "${forecast.main.temp}°C", fontSize = 20.sp, textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Format the date and day of the week
            val (formattedDate, dayOfWeek) = formatDate(forecast.dt_txt)

            // Display the day of the week
            Text(
                text = dayOfWeek, fontSize = 14.sp, textAlign = TextAlign.Center, color = Color.Gray
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

@Composable
fun CityCheckbox(city: City, selectedCities: MutableList<String>) {
    var isChecked by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                isChecked = !isChecked
                if (isChecked) {
                    selectedCities.add(city.cityName)
                } else {
                    selectedCities.remove(city.cityName)
                }
            }
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                if (isChecked) {
                    selectedCities.add(city.cityName)
                } else {
                    selectedCities.remove(city.cityName)
                }
            },
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = city.cityName,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherForecastComposeTheme {
        // MainScreen()
    }
}


data class City(
    @SerializedName("city") val cityName: String,
    @SerializedName("districts") val districts: List<String>
)

fun getDistrictNames(context: Context): List<City> {
    val jsonFileString = getJsonDataFromAsset(context, "city_list.json")
    val gson = Gson()
    val listCityType = object : TypeToken<List<City>>() {}.type
    return gson.fromJson(jsonFileString, listCityType)
}

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}