import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.max_Temp
import com.example.compose.min_Temp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Root
import com.example.weatherforecastcompose.repository.loadSelectedCities
import com.example.weatherforecastcompose.ui.components.AnimatedNavDrawerMenuButton
import com.example.weatherforecastcompose.ui.components.CheckboxList
import com.example.weatherforecastcompose.ui.components.DotIndicator
import com.example.weatherforecastcompose.ui.components.ExpandableCard
import com.example.weatherforecastcompose.ui.components.LottieWeatherAnimationView
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(viewModel: WeatherViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val cityNames = viewModel.getDistrictNames(context)
    val selectedCities = remember { mutableStateListOf<String>() }

    // Load previously selected cities from SharedPreferences
    selectedCities.addAll(loadSelectedCities(context))

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                cityNames = cityNames,
                selectedCities = selectedCities,
                context = context
            )
        },
        drawerState = drawerState
    ) {
        val pagerState = rememberPagerState(pageCount = { selectedCities.size })
        MainScaffold(
            viewModel = viewModel,
            selectedCities = selectedCities,
            pagerState = pagerState,
            drawerState = drawerState,
            scope = scope,
        )
    }
}

@Composable
fun DrawerContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    cityNames: List<String>,
    selectedCities: MutableList<String>,
    context: Context
) {
    ModalDrawerSheet(
        Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search cities") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            // Show selected cities regardless of search query
            items(selectedCities) { cityName ->
                CheckboxList(cityName, selectedCities, context = context)
                HorizontalDivider(color = Color.Gray)
            }

            // Show filtered cities based on search query
            if (searchQuery.isNotEmpty()) {
                items(cityNames.filter {
                    it.contains(searchQuery, ignoreCase = true)
                }) { cityName ->
                    if (!selectedCities.contains(cityName)) {
                        CheckboxList(cityName, selectedCities, context = context)
                        HorizontalDivider(color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScaffold(
    viewModel: WeatherViewModel,
    selectedCities: MutableList<String>,
    pagerState: PagerState,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                pagerState = pagerState,
                drawerState = drawerState,
                scope = scope
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                WeatherList(
                    viewModel = viewModel,
                    selectedCities = selectedCities,
                    pagerState = pagerState
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    pagerState: PagerState,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
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

                    AnimatedNavDrawerMenuButton(
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
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherList(
    viewModel: WeatherViewModel = hiltViewModel(),
    selectedCities: List<String>,
    pagerState: PagerState
) {
    LaunchedEffect(key1 = selectedCities.isNotEmpty()) {
        if (selectedCities.isNotEmpty()) {
            pagerState.scrollToPage(0)
        }
    }

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        val cityName = selectedCities.getOrNull(page)
        cityName?.let { city ->

            LaunchedEffect(key1 = city) {
                if (viewModel.weatherData[city] == null) {
                    viewModel.loadWeather(city)
                }
            }

            val weatherState = viewModel.weatherData[city]
            val errorMessageState = viewModel.errorMessages[city] ?: ""
            val isLoadingState = viewModel.isLoading[city] ?: false

            when {
                isLoadingState && weatherState == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessageState.isNotEmpty() -> {
                    RetryView(error = errorMessageState) {
                        viewModel.loadWeather(city)
                    }
                }

                weatherState != null -> {
                    WeatherUI(
                        viewModel = viewModel,
                        city,
                        weatherState.list.firstOrNull()?.main?.temp,
                        weatherState.list
                    )
                }

                else -> {
                    // Durum belirsizse bir şey yapma
                }
            }
        }
    }
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


@Composable
fun WeatherUI(
    viewModel: WeatherViewModel,
    city: String,
    currentTemp: Double?,
    forecast: List<Root>?
) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    val humidityResource = rememberIconResource(
        isSystemInDarkTheme(),
        R.drawable.humidity_light,
        R.drawable.humidity_dark
    )
    val windResource =
        rememberIconResource(isSystemInDarkTheme(), R.drawable.wind_light, R.drawable.wind_dark)
    val pressureResource = rememberIconResource(
        isSystemInDarkTheme(),
        R.drawable.pressure_light,
        R.drawable.pressure_dark
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
        //.verticalScroll(state),
        , verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherCard(
            city = city,
            currentTemp = currentTemp,
            forecast = forecast,
            viewModel = viewModel
        )
        WeatherDetailsCard(
            humidityResource = humidityResource,
            windResource = windResource,
            pressureResource = pressureResource,
            forecast = forecast
        )
        ForecastList(forecast = forecast, viewModel = viewModel)
    }
}

@Composable
fun WeatherCard(
    city: String,
    currentTemp: Double?,
    forecast: List<Root>?,
    viewModel: WeatherViewModel
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp)
    ) {
        Column(
            modifier = Modifier
                //.fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
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
                text = viewModel.formatDate(forecast?.get(0)?.dt_txt ?: "-").first,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
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

            if (currentTemp != null) {
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${forecast?.get(0)?.main?.temp_min}°",
                    fontSize = 21.sp,
                    color = min_Temp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${forecast?.get(0)?.main?.temp_max}°",
                    fontSize = 21.sp,
                    color = max_Temp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun WeatherDetailsCard(
    humidityResource: Painter,
    windResource: Painter,
    pressureResource: Painter,
    forecast: List<Root>?
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailColumn(
                iconResource = humidityResource,
                value = "${forecast?.get(0)?.main?.humidity}%",
                label = "Humidity",
                modifier = Modifier.weight(1f)
            )

            DetailColumn(
                iconResource = windResource,
                value = "${forecast?.get(0)?.wind?.speed?.toInt()} km/h",
                label = "Wind",
                modifier = Modifier.weight(1f)
            )

            DetailColumn(
                iconResource = pressureResource,
                value = "${forecast?.get(0)?.main?.pressure} hPa",
                label = "Pressure",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DetailColumn(
    iconResource: Painter,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = iconResource,
            contentDescription = label,
            modifier = Modifier
                .size(40.dp)
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
    }
}

@Composable
fun rememberIconResource(isDarkTheme: Boolean, lightIcon: Int, darkIcon: Int): Painter {
    return if (isDarkTheme) {
        painterResource(id = lightIcon)
    } else {
        painterResource(id = darkIcon)
    }
}

@Composable
fun ForecastList(
    forecast: List<Root>?,
    viewModel: WeatherViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(forecast?.slice(1..5) ?: emptyList()) { weather ->
            val date = viewModel.formatDate(weather.dt_txt).first
            val dayOfWeek = viewModel.formatDate(weather.dt_txt).second
            ExpandableCard(date = date, dayOfWeek = dayOfWeek, weather = weather)
            Spacer(modifier = Modifier.height(8.dp)) // Add space between items if needed
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
MainScreen(viewModel = hiltViewModel())
}