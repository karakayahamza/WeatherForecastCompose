package com.example.weatherforecastcompose.presentation.compenents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecastcompose.presentation.weather.WeatherViewModel

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    viewModel: WeatherViewModel = hiltViewModel(),
    onDrawerClosed: @Composable () -> Unit
) {
    // State to manage the search query locally
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(drawerState.isClosed) {
        if (drawerState.isClosed) {
            searchQuery = ""
        }
    }

    if (drawerState.isClosed) {
        onDrawerClosed()
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val selectedCities by viewModel.selectedCities.collectAsState()
    val cityList by viewModel.cityNames.collectAsState()

    ModalDrawerSheet(
        modifier = Modifier
            .widthIn(min = 280.dp, max = screenWidth * 0.8f)
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Ara..") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))


        if (searchQuery.isBlank()) {
            // Display selected cities when search query is empty
            if (selectedCities.isNotEmpty()) {
                Text(
                    text = "Selected Cities",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    items(selectedCities.toList()) { cityName -> // Convert Set to List
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = true,
                                onCheckedChange = { isChecked ->
                                    if (!isChecked) {
                                        viewModel.toggleCitySelection(
                                            cityName,
                                            false
                                        ) // Unselect city
                                    }
                                }
                            )
                            Text(
                                text = cityName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No selected cities.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
        } else {
            // Search results logic
            val filteredCities = cityList.filter { cityFromJson ->
                cityFromJson.city.contains(searchQuery, ignoreCase = true) &&
                        !selectedCities.contains(cityFromJson.city) // Exclude selected cities
            }

            if (filteredCities.isNotEmpty()) {
                Text(
                    text = "Search Results",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    items(filteredCities) { cityFromJson ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = false, // Since these are unselected cities
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        viewModel.toggleCitySelection(
                                            cityFromJson.city,
                                            true
                                        ) // Select city
                                        viewModel.addCity(cityFromJson.city)
                                        searchQuery = ""
                                    }
                                }
                            )
                            Text(
                                text = cityFromJson.city,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No cities found",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Created by hkarakaya",
            modifier = Modifier
                .padding(16.dp)
                .wrapContentWidth(),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    }
}
