package com.example.weatherforecastcompose.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weatherforecastcompose.presentation.compenents.WeatherPager
import com.example.weatherforecastcompose.presentation.weather.WeatherViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    pagerState: PagerState,
    cities: List<String>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (viewModel.weatherStates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Henüz şehir eklenmedi")
            }
        } else {
            WeatherPager(pagerState, cities, viewModel)
        }
    }
}
