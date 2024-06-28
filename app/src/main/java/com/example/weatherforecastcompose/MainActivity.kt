package com.example.weatherforecastcompose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.weatherforecastcompose.model.Root
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastComposeTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var searchQuery by remember { mutableStateOf("") }
                val context = this@MainActivity
                val cityNames = getDistrictNames(context)
                val selectedCities = remember { mutableStateListOf<String>() }

                // Load previously selected cities from SharedPreferences
                selectedCities.addAll(loadSelectedCities(context))

                // Filtered city names based on search query
                val filteredCityNames = remember(searchQuery) {
                    cityNames.filter { it.contains(searchQuery, ignoreCase = true) }
                }

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
                                items(filteredCityNames) { cityName ->
                                    CityCheckbox(cityName, selectedCities, context = context)
                                    HorizontalDivider(color = Color.Gray)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }, drawerState = drawerState
                ) {
                    Scaffold(

                        content = { paddingValues ->
                            Column(modifier = Modifier.padding(paddingValues)) {

                                val iconState = remember { mutableStateOf(false) }

                                // Bileşenleri yerleştir
                                AnimatedMenuButton(
                                    isOpen = drawerState.isOpen,
                                    onToggle = { iconState.value = !iconState.value
                                        scope.launch {
                                            if (drawerState.isOpen) {
                                                drawerState.close()
                                            } else {
                                                drawerState.open()
                                            }
                                        }
                                    }
                                )


                                WeatherList(
                                    viewModel = hiltViewModel(),
                                    selectedCities = selectedCities,
                                )
                            }
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun AnimatedMenuButton(
    isOpen: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = Dp.Unspecified
) {
    val rotation by animateFloatAsState(
        targetValue = if (isOpen) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    val icon: ImageVector = if (isOpen) Icons.Default.ArrowBack else Icons.Default.Menu
    val contentDescription: String = if (isOpen) "Back" else "Menu"

    IconButton(
        onClick = { onToggle() },
        modifier = modifier.padding(8.dp)
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherList(
    viewModel: WeatherViewModel = hiltViewModel(),
    selectedCities: List<String>
) {
    val pagerState = rememberPagerState(pageCount = { selectedCities.size })

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        val cityName = selectedCities[page]

        // ViewModel'den veri yükleme işlemi sadece gerekli durumda yapılıyor
        LaunchedEffect(cityName) {
            if (page == pagerState.currentPage &&
                (viewModel.weather.value == null || viewModel.weather.value?.city?.name != cityName)
            ) {
                viewModel.loadWeather(cityName)
            }
        }

        // ViewModel'den gelen state'leri kullanarak UI'ı oluştur
        val weatherState = viewModel.weather.value
        val errorMessageState = viewModel.errorMessage.value
        val isLoadingState = viewModel.isLoading.value

        when {
            isLoadingState && weatherState == null -> {
                // Yükleme durumunda progress göster
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessageState.isNotEmpty() -> {
                // Hata durumunda tekrar deneme butonu göster
                RetryView(error = errorMessageState) {
                    viewModel.loadWeather(cityName)
                }
            }
            weatherState != null -> {
                // Veri yüklendiğinde hava durumu UI'ı göster
                WeatherUI(cityName, weatherState.list.firstOrNull()?.main?.temp, weatherState.list)
            }
            else -> {
                // Durum belirsizse bir şey yapma
            }
        }
    }
}






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

@SuppressLint("DefaultLocale")
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
    val painter = rememberAsyncImagePainter(
        "https://openweathermap.org/img/wn/${
            forecast?.get(0)?.weather?.get(
                0
            )?.icon
        }@2x.png"
    )

    val humidityResource: Painter = if (MaterialTheme.colorScheme.primary.isSpecified) {
        painterResource(id = R.drawable.humidity_light)
    } else {
        painterResource(id = R.drawable.humidity_dark)
    }

    val windResource: Painter = if (MaterialTheme.colorScheme.primary.isSpecified) {
        painterResource(id = R.drawable.wind_light)
    } else {
        painterResource(id = R.drawable.wind_dark)
    }

    val pressureResource: Painter = if (MaterialTheme.colorScheme.primary.isSpecified) {
        painterResource(id = R.drawable.pressure_light)
    } else {
        painterResource(id = R.drawable.pressure_dark)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight(unbounded = true) // Allow the card to wrap content height
                .clip(RoundedCornerShape(16.dp))
                .shadow(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = city,
                    fontSize = if (city.length > 20 || city.contains(" ")) 30.sp else 45.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
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
                if (currentTemp != null) {
                    Text(
                        text = "${currentTemp.toInt()} °C",
                        fontSize = 42.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight(unbounded = true) // Allow the card to wrap content height
                .clip(RoundedCornerShape(16.dp))
                .shadow(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = humidityResource,
                        contentDescription = "Humidity Image",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                    Text(
                        text = "${forecast?.get(0)?.main?.humidity?.toInt()}%",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Humidity",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = windResource,
                        contentDescription = "Wind Image",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                    Text(
                        text = "${forecast?.get(0)?.wind?.speed?.toInt()} km/h",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Wind",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = pressureResource,
                        contentDescription = "Pressure Image",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                    Text(
                        text = "${forecast?.get(0)?.main?.pressure?.toInt()} hPa",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Pressure",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(forecast?.take(5) ?: emptyList()) { weather ->

                // weather.wind.speed
                // weather.main.humidity
                // weather.main.pressure

                val date = formatDate(weather.dt_txt).first
                val dayOfWeek = formatDate(weather.dt_txt).second

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shadow(4.dp),

                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = date,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = dayOfWeek,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${weather.main.temp.toInt()}°C",
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherForecastComposeTheme {
        // MainScreen()
    }
}

@Composable
fun CityCheckbox(city: String, selectedCities: MutableList<String>, context: Context) {
    val isChecked = remember(city) {
        mutableStateOf(selectedCities.contains(city))
    }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                isChecked.value = !isChecked.value
                if (isChecked.value) {
                    selectedCities.add(0, city)
                } else {
                    selectedCities.remove(city)
                }
                saveSelectedCities(selectedCities, context)
            }) {
        Checkbox(
            checked = isChecked.value, onCheckedChange = {
                isChecked.value = it
                if (isChecked.value) {
                    selectedCities.add(0, city)
                } else {
                    selectedCities.remove(city)
                }
                saveSelectedCities(selectedCities, context)
            }, modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = city, fontSize = 16.sp, modifier = Modifier.fillMaxWidth(), color = Color.White
        )
    }
}

fun saveSelectedCities(selectedCities: List<String>, context: Context) {

    for (i in selectedCities){
        println("SelectedCities: $i")
    }

    val sharedPreferences = context.getSharedPreferences("selected_cities", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putStringSet("cities", HashSet(selectedCities))
    editor.apply()
}

fun loadSelectedCities(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences("selected_cities", Context.MODE_PRIVATE)
    val savedCities = sharedPreferences.getStringSet("cities", HashSet()) ?: HashSet()
    return savedCities.toList()
}


data class City(
    @SerializedName("city") val cityName: String,
    @SerializedName("districts") val districts: List<String>
)

fun getDistrictNames(context: Context): List<String> {
    val jsonFileString = getJsonDataFromAsset(context, "city_list.json")
    val gson = Gson()
    val listCityType = object : TypeToken<List<City>>() {}.type
    val cities: List<City> = gson.fromJson(jsonFileString, listCityType)

    // Extract district names into a single list
    val districtNames = mutableListOf<String>()
    cities.forEach { city ->
        districtNames.addAll(city.districts)
    }
    return districtNames
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
