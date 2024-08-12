package com.example.weatherforecastcompose.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.weatherforecastcompose.model.Main
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.repository.loadSelectedCities
import com.example.weatherforecastcompose.view.components.DrawerContent
import com.example.weatherforecastcompose.view.components.LoadingIndicator
import com.example.weatherforecastcompose.view.components.MainTopAppBar
import com.example.weatherforecastcompose.view.components.NoInternetConnectionMessage
import com.example.weatherforecastcompose.view.components.WeatherContent
import com.example.weatherforecastcompose.view.components.WeatherRetryView
import com.example.weatherforecastcompose.view.components.calculateCurrentOffsetForPage
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun WeatherMainScreen(viewModel: WeatherViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val selectedCities = remember { mutableStateListOf<String>() }
    val currentLatLon = remember { mutableStateOf<Pair<Double, Double>?>(null) }
    selectedCities.addAll(loadSelectedCities(context))

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                loadCurrentLocationCity(context) { latLon ->
                    currentLatLon.value = latLon
                    currentLatLon.value?.let { (lat, lon) ->
                        viewModel.loadCurrentWeather(lat.toString(), lon.toString())
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(
            drawerState = drawerState,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedCities = selectedCities,
            context = context,
            onDrawerClosed = {
                searchQuery = ""
                LocalSoftwareKeyboardController.current?.hide()
            }
        )
    }) {
        val citiesToDisplay = if (viewModel.currentCityName != null && viewModel.currentWeatherData != null) {
            // Display current location's weather first, followed by selected cities
            listOf(viewModel.currentCityName!!) + selectedCities
        } else {
            selectedCities
        }

        WeatherMainStructure(
            viewModel = viewModel,
            selectedCities = citiesToDisplay,
            drawerState = drawerState,
            currentWeatherData = viewModel.currentWeatherData
        )
    }
}


@SuppressLint("MissingPermission")
fun loadCurrentLocationCity(context: Context, onCityFound: (Pair<Double, Double>) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onCityFound(Pair(it.latitude, it.longitude))
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherMainStructure(
    viewModel: WeatherViewModel,
    selectedCities: List<String>,
    drawerState: DrawerState,
    currentWeatherData: WeatherModel?
) {
    val pagerState = rememberPagerState(pageCount = { selectedCities.size })
    val scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        MainTopAppBar(
            pagerState = pagerState,
            drawerState = drawerState,
            scope = scope,
            backgroundColor = Color.Transparent
        )
    }, content = { paddingValues ->
        WeatherPager(
            viewModel = viewModel,
            selectedCities = selectedCities,
            pagerState = pagerState,
            modifier = Modifier
                .padding(paddingValues),
            currentWeatherData = currentWeatherData
        )
    })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherPager(
    viewModel: WeatherViewModel,
    selectedCities: List<String>,
    pagerState: PagerState,
    modifier: Modifier,
    currentWeatherData: WeatherModel?
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
        state = pagerState, modifier = modifier.fillMaxSize(), verticalAlignment = Alignment.Top
    ) { page ->
        Box(
            Modifier
                .graphicsLayer {
                    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                    translationX = pageOffset * size.width
                    alpha = 1 - pageOffset.absoluteValue
                }
        ) {
            val cityName = selectedCities.getOrNull(page)
            if (page == 0 && currentWeatherData != null) {
                // Display the current location's weather data
                WeatherPageContent(viewModel = viewModel, city = "Current Location", weatherData = currentWeatherData)
            } else {
                cityName?.let { city ->
                    WeatherPageContent(viewModel = viewModel, city = city)
                }
            }
        }
    }
}

@Composable
fun WeatherPageContent(
    viewModel: WeatherViewModel,
    city: String,
    weatherData: WeatherModel? = null
) {
    val weatherState = if (city == "Current Location") weatherData else viewModel.weatherData[city]
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
