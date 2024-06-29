package com.example.weatherforecastcompose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.compose.WeatherForecastComposeTheme
import com.example.compose.max_Temp
import com.example.compose.min_Temp
import com.example.weatherforecastcompose.model.Root
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalFoundationApi::class)
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
                                // Show selected cities regardless of search query
                                items(selectedCities) { cityName ->
                                    CityCheckbox(cityName, selectedCities, context = context)
                                    HorizontalDivider(color = Color.Gray)
                                }

                                // Show filtered cities based on search query
                                if (searchQuery.isNotEmpty()) {
                                    items(cityNames.filter {
                                        it.contains(
                                            searchQuery,
                                            ignoreCase = true
                                        )
                                    }) { cityName ->
                                        if (!selectedCities.contains(cityName)) {
                                            CityCheckbox(
                                                cityName,
                                                selectedCities,
                                                context = context
                                            )
                                            HorizontalDivider(color = Color.Gray)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }, drawerState = drawerState
                ) {
                    val pagerState = rememberPagerState(pageCount = { selectedCities.size })

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier = Modifier.weight(1f))

                                        Box(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            DotIndicator(
                                                pagerState = pagerState,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }

                                        Box(
                                            modifier = Modifier.weight(1f),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            val iconState = remember { mutableStateOf(false) }

                                            AnimatedMenuButton(
                                                isOpen = drawerState.isOpen,
                                                onToggle = {
                                                    iconState.value = !iconState.value
                                                    scope.launch {
                                                        if (drawerState.isOpen) {
                                                            drawerState.close()
                                                        } else {
                                                            drawerState.open()
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                },
                            )
                        },
                        content = { paddingValues ->
                            Column(modifier = Modifier.padding(paddingValues)) {
                                WeatherList(
                                    viewModel = hiltViewModel(),
                                    selectedCities = selectedCities,
                                    pagerState = pagerState
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { pageIndex ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (pageIndex == pagerState.currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                    .padding(horizontal = 2.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}


@Composable
fun AnimatedMenuButton(isOpen: Boolean, onToggle: () -> Unit) {
    val transitionSpec = tween<Float>(durationMillis = 100, easing = LinearOutSlowInEasing)

    val rotationAngle by animateFloatAsState(
        targetValue = if (isOpen) 90f else 0f,
        animationSpec = transitionSpec
    )

    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isOpen) Icons.Default.Close else Icons.Default.Menu,
            contentDescription = if (isOpen) "Close menu" else "Open menu",
            modifier = Modifier.graphicsLayer(rotationZ = rotationAngle)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherList(
    viewModel: WeatherViewModel = hiltViewModel(),
    selectedCities: List<String>,
    pagerState: PagerState
) {
    // En başta seçili şehir listesi boş değilse ilk sayfaya kaydır
    LaunchedEffect(key1 = selectedCities.isNotEmpty()) {
        if (selectedCities.isNotEmpty()) {
            pagerState.scrollToPage(0)
        }
    }

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        val cityName = selectedCities.getOrNull(page)
        cityName?.let { city ->
            // ViewModel'den veri yükleme işlemi sadece gerekli durumda yapılıyor
            LaunchedEffect(key1 = city) {
                if (viewModel.weatherData[city] == null) {
                    viewModel.loadWeather(city)
                }
            }

            // ViewModel'den gelen state'leri kullanarak UI'ı oluştur
            val weatherState = viewModel.weatherData[city]
            val errorMessageState = viewModel.errorMessages[city] ?: ""
            val isLoadingState = viewModel.isLoading[city] ?: false

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
                        viewModel.loadWeather(city)
                    }
                }

                weatherState != null -> {
                    // Veri yüklendiğinde hava durumu UI'ı göster
                    WeatherUI(city, weatherState.list.firstOrNull()?.main?.temp, weatherState.list)
                }

                else -> {
                    // Durum belirsizse bir şey yapma
                }
            }
        }
    }

//    // Yeni bir şehir eklendiğinde HorizontalPager'ı en sona kaydır
//    LaunchedEffect(key1 = selectedCities.size) {
//        if (selectedCities.isNotEmpty()) {
//            pagerState.animateScrollToPage(selectedCities.size - 1)
//        }
//    }
}

@Composable
fun RetryView(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = error)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
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

    val humidityResource: Painter = if (isSystemInDarkTheme()) {
        painterResource(id = R.drawable.humidity_light)
    } else {
        painterResource(id = R.drawable.humidity_dark)
    }

    val windResource: Painter = if (isSystemInDarkTheme()) {
        painterResource(id = R.drawable.wind_light)
    } else {
        painterResource(id = R.drawable.wind_dark)
    }

    val pressureResource: Painter = if (isSystemInDarkTheme()) {
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
                .wrapContentHeight(unbounded = true)
                .clip(RoundedCornerShape(16.dp))
                .shadow(8.dp)
                .align(Alignment.CenterHorizontally),

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
                    fontSize = 30.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatDate(forecast?.get(0)?.dt_txt ?: "-").first,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .size(78.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = forecast?.get(0)?.weather?.get(0)?.description!!.uppercase(),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }


                if (currentTemp != null) {
                    Row(
                        modifier = Modifier

                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = currentTemp.toInt().toString(),
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

                Text(
                    text = "Hissedilen: ${forecast?.get(0)?.main?.feels_like} °C",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "${forecast?.get(0)?.main?.temp_min}°",
                        fontSize = 21.sp,
                        color = min_Temp,
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${forecast?.get(0)?.main?.temp_max}°",
                        fontSize = 21.sp,
                        color = max_Temp,
                        modifier = Modifier
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
                            .size(40.dp)
                            .padding(8.dp)
                    )
                    Text(
                        text = "${forecast?.get(0)?.main?.humidity}%",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Humidity",
                        fontSize = 10.sp,
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
                            .size(40.dp)
                            .padding(8.dp)
                    )
                    Text(
                        text = "${forecast?.get(0)?.wind?.speed?.toInt()} km/h",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Wind",
                        fontSize = 10.sp,
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
                            .size(40.dp)
                            .padding(8.dp)
                    )
                    Text(
                        text = "${forecast?.get(0)?.main?.pressure?.toInt()} hPa",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Pressure",
                        fontSize = 10.sp,
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
            items(forecast?.take(forecast.size) ?: emptyList()) { weather ->

                // weather.wind.speed
                // weather.main.humidity
                // weather.main.pressure

                val date = weather.dt_txt?.let { formatDate(it).first }
                val dayOfWeek = weather.dt_txt?.let { formatDate(it).second }

                if (dayOfWeek != null) {
                    if (date != null) {
                        ExpandableCard(date = date, dayOfWeek = dayOfWeek, weather = weather)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(
    date: String,
    dayOfWeek: String,
    weather: Root // Assuming Weather is a data class
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(if (expanded) 180f else 0f)



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .shadow(4.dp)
            .clickable { expanded = !expanded } // Toggles expanded state
            .animateContentSize() // Animates size change when expanded
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (expanded) 8.dp else 0.dp),
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
                    text = "${weather.main?.temp?.toInt()}°C",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotationAngle)
                        .clickable { expanded = !expanded }
                )
            }

            // Expanded content
            if (expanded) {
                Text(
                    text = "Detailed weather information here...",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row {
                    Text(
                        text = "Detailed weather information here...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                // Add more detailed content here as needed
            }
        }
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
    val json = Gson().toJson(selectedCities)
    val sharedPreferences = context.getSharedPreferences("selected_cities", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("cities", json)
    editor.apply()
}

fun loadSelectedCities(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences("selected_cities", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("cities", null)
    return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: emptyList()

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}