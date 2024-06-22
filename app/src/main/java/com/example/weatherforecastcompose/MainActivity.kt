package com.example.weatherforecastcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

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
    println("-----1------ ")

    NavHost(navController, startDestination = "crypto_list") {
        composable(
            "crypto_list",
            arguments = listOf(navArgument("name") { type = NavType.StringType; defaultValue = "Izmir" })
        ) { backStackEntry ->
            CryptoList(viewModel = hiltViewModel(
                //backStackEntry = backStackEntry,
                viewModelStoreOwner = backStackEntry
            ))
        }
    }
}

@Composable
fun CryptoList(viewModel: WeatherViewModel = hiltViewModel()) {
    val cryptoList by remember { viewModel.weather }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }
    println("-----2------ ")

    if (isLoading) {
        println("-----3------ ")

        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
    if (errorMessage.isNotEmpty()) {
        println("-----${errorMessage}------ ")

        viewModel.loadWeather("Izmir")

        cryptoList?.main?.let { println("----- ${it.temp_min} ------ ") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherForecastComposeTheme {
        MainScreen(modifier = Modifier.padding())
    }
}