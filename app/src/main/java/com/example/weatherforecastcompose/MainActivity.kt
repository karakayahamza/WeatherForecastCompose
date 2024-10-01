package com.example.weatherforecastcompose

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecastcompose.presentation.compenents.DrawerContent
import com.example.weatherforecastcompose.presentation.compenents.MainTopAppBar
import com.example.weatherforecastcompose.presentation.screens.MainScreen
import com.example.weatherforecastcompose.presentation.ui.theme.WeatherForecastComposeTheme
import com.example.weatherforecastcompose.presentation.weather.WeatherViewModel
import com.example.weatherforecastcompose.util.WeatherUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: WeatherViewModel = hiltViewModel()
            val cities = viewModel.cities
            val pagerState = rememberPagerState(pageCount = { cities.size })
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val currentPlace = remember { mutableStateOf<Pair<Double, Double>?>(null) }

            LocationInfoScreen(this, viewModel, currentPlace)

            WeatherForecastComposeTheme {
                Scaffold(topBar = {
                    MainTopAppBar(
                        pagerState = pagerState,
                        drawerState = drawerState,
                        backgroundColor = Color.Transparent,
                        currentPlace = currentPlace.value
                    )
                }) { innerPadding ->
                    ModalNavigationDrawer(modifier = Modifier.padding(innerPadding),
                        drawerState = drawerState,
                        drawerContent = {
                            DrawerContent(drawerState, viewModel, onDrawerClosed = { /* -_- */ })
                        }) {
                        MainScreen(
                            viewModel = viewModel, pagerState = pagerState, cities = cities
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(context: Context, onLocationRetrieved: (Location?) -> Unit) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val locationClient = LocationServices.getFusedLocationProviderClient(context)


    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    if (permissionState.allPermissionsGranted) {
        // Set a longer interval and minimum movement threshold for location updates
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 60000L) // 60 seconds
                .setMinUpdateIntervalMillis(30000L) // 30 seconds
                .setMaxUpdateDelayMillis(60000L) // 60 seconds
                .setMinUpdateDistanceMeters(10f).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.firstOrNull()?.let { onLocationRetrieved(it) }
            }
        }

        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        if (ActivityCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_COARSE_LOCATION
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
fun LocationInfoScreen(
    context: Context, viewModel: WeatherViewModel, currentPlace: MutableState<Pair<Double, Double>?>
) {
    RequestLocationPermission(context) { location ->
        location?.let {
            val (lat, lon) = it.latitude to it.longitude
            val newPlace = lat to lon

            if (currentPlace.value == null || WeatherUtils.distanceBetween(
                    currentPlace.value!!, newPlace
                ) > 100
            ) {
                currentPlace.value = newPlace
                viewModel.getWeatherByLocation(lat, lon)
            } else {
                Log.d("LocationScreen", "Location already present: $newPlace")
            }
        } ?: run {
            Toast.makeText(
                context, "GPS kapalı veya konum mevcut değil", Toast.LENGTH_LONG
            ).show()
        }
    }
}


