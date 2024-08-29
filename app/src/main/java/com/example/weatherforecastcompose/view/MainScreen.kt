package com.example.weatherforecastcompose.view

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.model.WeatherModel
import com.example.weatherforecastcompose.repository.loadSelectedCities
import com.example.weatherforecastcompose.view.components.DrawerContent
import com.example.weatherforecastcompose.view.components.LoadingIndicator
import com.example.weatherforecastcompose.view.components.LocationInfoScreen
import com.example.weatherforecastcompose.view.components.MainTopAppBar
import com.example.weatherforecastcompose.view.components.WeatherContent
import com.example.weatherforecastcompose.view.components.WeatherRetryView
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun WeatherMainScreen(viewModel: WeatherViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val selectedCities = remember { mutableStateListOf<String>() }
    val currentPlace = remember { mutableStateOf<Pair<Double, Double>?>(null) }

    selectedCities.addAll(loadSelectedCities(context).filterNot { it in selectedCities })

    LocationInfoScreen(context, viewModel, currentPlace)

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
        beyondBoundsPageCount = 3
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
    val formattedCityName = city.split(",").let {
        if (it.size > 1) {
            "${it[1].trim()}, ${it[0].trim()}"
        } else {
            it[0].trim()
        }
    }

    LaunchedEffect(formattedCityName) {
        if (viewModel.weatherData[formattedCityName] == null) {
            viewModel.loadWeather(formattedCityName)
        }
    }

    val weatherState = viewModel.weatherData[formattedCityName]
    val errorMessage = viewModel.errorMessages[formattedCityName] ?: ""
    val isLoading = viewModel.isLoading[formattedCityName] ?: false

    when {
        isLoading -> LoadingIndicator()
        errorMessage.isNotEmpty() && weatherState == null -> WeatherRetryView(error = errorMessage) {
            viewModel.loadWeather(formattedCityName)
        }

        weatherState != null -> WeatherContent(
            currentTemp = weatherState.list.firstOrNull()?.main?.temp,
            forecast = weatherState,
        )
    }
}