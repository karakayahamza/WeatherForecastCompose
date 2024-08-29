package com.example.weatherforecastcompose.view.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import com.example.weatherforecastcompose.util.distanceBetween
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

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
        // Set a longer interval and minimum movement threshold for location updates
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 60000L) // 60 seconds
                .setMinUpdateIntervalMillis(30000L) // 30 seconds
                .setMaxUpdateDelayMillis(60000L) // 60 seconds
                .setMinUpdateDistanceMeters(10f)
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
fun LocationInfoScreen(
    context: Context,
    viewModel: WeatherViewModel,
    currentPlace: MutableState<Pair<Double, Double>?>
) {
    RequestLocationPermission(context) { location ->
        location?.let {
            val (lat, lon) = it.latitude to it.longitude
            val newPlace = lat to lon

            if (currentPlace.value == null || distanceBetween(
                    currentPlace.value!!,
                    newPlace
                ) > 100
            ) {
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