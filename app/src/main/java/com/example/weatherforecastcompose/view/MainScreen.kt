package com.example.weatherforecastcompose.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.repository.loadSelectedCities
import com.example.weatherforecastcompose.view.components.DrawerContent
import com.example.weatherforecastcompose.view.components.LoadingIndicator
import com.example.weatherforecastcompose.view.components.MainTopAppBar
import com.example.weatherforecastcompose.view.components.WeatherContent
import com.example.weatherforecastcompose.view.components.WeatherRetryView
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch

@Composable
fun WeatherMainScreen(viewModel: WeatherViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val selectedCities = remember { mutableStateListOf<String>() }
    val currentPlace = remember { mutableStateOf<Pair<Double, Double>?>(null) }

    selectedCities.addAll(loadSelectedCities(context).filterNot { it in selectedCities })

    LocationScreen(context, viewModel, currentPlace)

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(
            drawerState = drawerState,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedCities = selectedCities,
            context = context,
            onDrawerClosed = {
                LocalSoftwareKeyboardController.current?.hide()
            }
        )
    }) {
        WeatherMainStructure(
            viewModel = viewModel,
            selectedCities = selectedCities,
            drawerState = drawerState,
            currentPlace = currentPlace.value
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherMainStructure(
    viewModel: WeatherViewModel,
    selectedCities: List<String>,
    drawerState: DrawerState,
    currentPlace: Pair<Double, Double>?
) {
    val citiesWithCurrentWeather = buildList {
        viewModel.currentWeatherData?.let { add("currentWeather") }
        addAll(selectedCities)
    }

    val pagerState = rememberPagerState(pageCount = { citiesWithCurrentWeather.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(citiesWithCurrentWeather.size) {
        pagerState.scrollToPage(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopAppBar(
                pagerState = pagerState,
                drawerState = drawerState,
                scope = scope,
                backgroundColor = Color.Transparent,
                currentPlace = currentPlace
            )
        },
        content = { paddingValues ->
            WeatherPager(
                viewModel = viewModel,
                citiesWithCurrentWeather = citiesWithCurrentWeather,
                pagerState = pagerState,
                modifier = Modifier
                    .padding(paddingValues)
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherPager(
    viewModel: WeatherViewModel,
    citiesWithCurrentWeather: List<String>,
    pagerState: PagerState,
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(citiesWithCurrentWeather) {
        if (citiesWithCurrentWeather.isNotEmpty()) {
            coroutineScope.launch {
                pagerState.scrollToPage(0)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        userScrollEnabled = true,
        pageSpacing = 0.dp,
        beyondBoundsPageCount = 2
    ) { page ->
        when (val pageIdentifier = citiesWithCurrentWeather.getOrNull(page)) {
            "currentWeather" -> WeatherCurrentContent(viewModel.currentWeatherData)
            else -> {
                pageIdentifier?.let { WeatherPageContent(viewModel, it) }
            }
        }
    }
}


@Composable
fun WeatherCurrentContent(weatherData: WeatherModel?) {
    if (weatherData != null) {
        WeatherContent(
            currentTemp = weatherData.list.firstOrNull()?.main?.temp,
            forecast = weatherData,
        )
    } else {
        LoadingIndicator()
    }
}

@Composable
fun WeatherPageContent(viewModel: WeatherViewModel, city: String) {
    LaunchedEffect(city) {
        if (viewModel.weatherData[city] == null) {
            viewModel.loadWeather(city)
        }
    }

    val weatherState = viewModel.weatherData[city]
    val errorMessage = viewModel.errorMessages[city] ?: ""
    val isLoading = viewModel.isLoading[city] ?: false

    when {
        isLoading -> LoadingIndicator()
        errorMessage.isNotEmpty() && weatherState == null -> WeatherRetryView(error = errorMessage) {
            viewModel.loadWeather(city)
        }

        weatherState != null -> WeatherContent(
            currentTemp = weatherState.list.firstOrNull()?.main?.temp,
            forecast = weatherState,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(context: Context, onLocationRetrieved: (Location?) -> Unit) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val locationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    if (permissionState.allPermissionsGranted) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
            .setMinUpdateIntervalMillis(5000L)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.firstOrNull()?.let { onLocationRetrieved(it) }
            }
        }

        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.lastLocation.addOnSuccessListener { location ->
                onLocationRetrieved(location ?: run {
                    Toast.makeText(
                        context,
                        "GPS kapalı veya konum verisi kullanılamıyor. Lütfen konum servisini açınız.",
                        Toast.LENGTH_LONG
                    ).show()
                    null
                })
            }
        }
    } else {
        Column {
            Text("Lokasyon izni gerekli.")
            Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                Text("İzin Ver.")
            }
        }
    }
}

@Composable
fun LocationScreen(
    context: Context,
    viewModel: WeatherViewModel,
    currentPlace: MutableState<Pair<Double, Double>?>
) {
    RequestLocationPermission(context) { location ->
        location?.let {
            val (lat, lon) = it.latitude to it.longitude
            val newPlace = lat to lon

            if (currentPlace.value != newPlace) {
                currentPlace.value = newPlace
                viewModel.loadCurrentWeather(lat.toString(), lon.toString())
            } else {
                Log.d("LocationScreen", "Location already present: $newPlace")
            }
        } ?: run {
            Toast.makeText(
                context,
                "GPS kapalı veya konum mevcut değil",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
