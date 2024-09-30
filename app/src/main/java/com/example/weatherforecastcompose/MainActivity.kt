package com.example.weatherforecastcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecastcompose.presentation.compenents.DrawerContent
import com.example.weatherforecastcompose.presentation.compenents.MainTopAppBar
import com.example.weatherforecastcompose.presentation.screens.MainScreen
import com.example.weatherforecastcompose.presentation.weather.WeatherViewModel
import com.example.weatherforecastcompose.presentation.ui.theme.WeatherForecastComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherForecastComposeTheme {
                val viewModel: WeatherViewModel = hiltViewModel()
                val cities = viewModel.cities
                val pagerState = rememberPagerState(pageCount = { cities.size })
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


                Scaffold(
                    topBar = {
                        MainTopAppBar(
                            pagerState = pagerState,
                            drawerState = drawerState,
                            backgroundColor = Color.Transparent,
                            currentPlace = null
                        )
                    }
                ) { innerPadding ->
                    ModalNavigationDrawer(
                        modifier = Modifier.padding(innerPadding),
                        drawerState = drawerState,
                        drawerContent = {
                            DrawerContent(
                                drawerState,
                                viewModel,
                                onDrawerClosed = { /* -_- */ })
                        }
                    ) {
                        MainScreen(
                            viewModel = viewModel,
                            pagerState = pagerState,
                            cities = cities
                        )
                    }

                }

            }
        }
    }
}
