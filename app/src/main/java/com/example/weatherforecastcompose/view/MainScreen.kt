package com.example.weatherforecastcompose.view

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
import com.example.weatherforecastcompose.repository.loadSelectedCities
import com.example.weatherforecastcompose.view.components.DrawerContent
import com.example.weatherforecastcompose.view.components.LoadingIndicator
import com.example.weatherforecastcompose.view.components.MainTopAppBar
import com.example.weatherforecastcompose.view.components.NoInternetConnectionMessage
import com.example.weatherforecastcompose.view.components.WeatherContent
import com.example.weatherforecastcompose.view.components.WeatherRetryView
import com.example.weatherforecastcompose.view.components.calculateCurrentOffsetForPage
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

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerContent(drawerState = drawerState,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            cityNames = cityNames,
            selectedCities = selectedCities,
            context = context,
            onDrawerClosed = {
                searchQuery = ""
                LocalSoftwareKeyboardController.current?.hide()
            })
    }) {
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
                .padding(paddingValues)
        )
    })
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