package com.example.weatherforecastcompose.presentation.compenents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.presentation.weather.WeatherViewModel


@Composable
fun WeatherPager(pagerState: PagerState, cities: List<String>, viewModel: WeatherViewModel) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        userScrollEnabled = true,
        pageSpacing = 0.dp,
        beyondViewportPageCount = 3
    ) { page ->
        val city = cities[page]
        val state = viewModel.weatherStates[city]
        when {
            state?.isLoading == true -> {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }

            state?.error?.isNotEmpty() == true -> {
                ErrorMessage(state.error)
            }

            state != null -> {
                WeatherPage(weatherState = state)
            }
        }
    }
}