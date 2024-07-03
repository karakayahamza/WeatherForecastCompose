import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.compose.max_Temp
import com.example.compose.min_Temp
import com.example.weatherforecastcompose.model.Root
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.repository.loadSelectedCities
import com.example.weatherforecastcompose.ui.components.DrawerContent
import com.example.weatherforecastcompose.ui.components.ExpandableCard
import com.example.weatherforecastcompose.ui.components.LottieWeatherAnimationView
import com.example.weatherforecastcompose.ui.components.MainTopAppBar
import com.example.weatherforecastcompose.ui.components.calculateCurrentOffsetForPage
import com.example.weatherforecastcompose.ui.weatherResources
import com.example.weatherforecastcompose.util.DateUtils.formatDate
import com.example.weatherforecastcompose.util.DateUtils.formatTime
import com.example.weatherforecastcompose.util.getWindDirection
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherMainScreen(viewModel: WeatherViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val cityNames = viewModel.getDistrictNames(context)
    val selectedCities = remember { mutableStateListOf<String>() }
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
        WeatherMainStructure(
            viewModel = viewModel,
            selectedCities = selectedCities,
            pagerState = pagerState,
            drawerState = drawerState,
            scope = scope
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherMainStructure(
    viewModel: WeatherViewModel,
    selectedCities: List<String>,
    pagerState: PagerState,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopAppBar(
                pagerState = pagerState,
                drawerState = drawerState,
                scope = scope,
                backgroundColor = Color.Transparent
            )
        },
        content = { paddingValues ->
            WeatherPager(
                viewModel = viewModel,
                selectedCities = selectedCities,
                pagerState = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherPager(
    viewModel: WeatherViewModel,
    selectedCities: List<String>,
    pagerState: PagerState,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = selectedCities.isNotEmpty()) {
        if (selectedCities.isNotEmpty()) {
            coroutineScope.launch {
                pagerState.scrollToPage(0)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) { page ->

        Box(Modifier
            .graphicsLayer {
                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                translationX = pageOffset * size.width
                alpha = 1 - pageOffset.absoluteValue
            }
            .fillMaxSize()) {
            val cityName = selectedCities.getOrNull(page)
            cityName?.let { city ->
                WeatherPageContent(viewModel = viewModel, city = city)
            }

        }
    }
}


@Composable
fun WeatherPageContent(viewModel: WeatherViewModel, city: String) {
    LaunchedEffect(key1 = city) {
        if (viewModel.weatherData[city] == null) {
            viewModel.loadWeather(city)
        }
    }

    val weatherState = viewModel.weatherData[city]
    val errorMessageState = viewModel.errorMessages[city] ?: ""
    val isLoadingState = viewModel.isLoading[city] ?: false

    when {

        isLoadingState && weatherState == null -> LoadingIndicator()

        errorMessageState.isNotEmpty() -> WeatherRetryView(error = errorMessageState) {
            viewModel.loadWeather(city)
        }

        weatherState != null -> {

            WeatherContent(
                city = city,
                currentTemp = weatherState.list.firstOrNull()?.main?.temp,
                forecast = weatherState
            )
        }

        else -> NoInternetConnectionMessage()
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}


@Composable
fun NoInternetConnectionMessage() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Lütfen İnternet Bağlantınızı Kontrol Edip Tekrar Deneyin",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
    }
}

@Composable
fun WeatherRetryView(error: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = error)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text(text = "Yenile")
            }
        }
    }
}

@Composable
fun WeatherContent(
    city: String,
    currentTemp: Double?,
    forecast: WeatherModel?
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        forecast?.let {
            WeatherMainCard(city, currentTemp, it.list)
            WeatherDetailsCard(
                humidityResource = weatherResources().humidityResource,
                windResource = weatherResources().windResource,
                pressureResource = weatherResources().pressureResource,
                forecast = forecast.list
            )
            SunriseSunsetInfo(it.city.sunrise, it.city.sunset)
            WeatherHourlyDetailsCard(it.list)

        }
    }
}

@Composable
fun WeatherMainCard(
    city: String, currentTemp: Double?, forecast: List<Root>?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
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
                text = formatDate(forecast?.get(0)?.dt_txt ?: "-").first,
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

            MinMaxTemperatureInfo(forecast)
        }
    }
}

@Composable
fun TemperatureInfo(currentTemp: Double) {
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

@Composable
fun MinMaxTemperatureInfo(forecast: List<Root>?) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "${forecast?.get(0)?.main?.temp_min}°",
            fontSize = 21.sp,
            color = min_Temp,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "${forecast?.get(0)?.main?.temp_max}°",
            fontSize = 21.sp,
            color = max_Temp,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun WeatherHourlyDetailsCard(forecast: List<Root>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(forecast.slice(1..5)) { weather ->
            val (date) = formatDate(weather.dt_txt)
            ExpandableCard(date = date, weather = weather)
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}


@Composable
fun SunriseSunsetInfo(sunrise: Long, sunset: Long) {
    val sunriseTime = formatTime(sunrise)
    val sunsetTime = formatTime(sunset)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SunriseSunsetColumn(
            label = "Gün Doğumu",
            time = sunriseTime,
            jsonFileName = "sunrise.json"
        )
        Spacer(modifier = Modifier.width(16.dp)) // Add space between columns
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
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.Asset(jsonFileName)
        )
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .size(55.dp)
                .padding(vertical = 5.dp)
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun WeatherDetailsCard(
    humidityResource: Painter,
    windResource: Painter,
    pressureResource: Painter,
    forecast: List<Root>?
) {

    Row(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
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
                    value = "${forecast?.get(0)?.wind?.deg?.let { getWindDirection(it) }},${
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
