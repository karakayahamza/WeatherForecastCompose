package com.example.weatherforecastcompose.view.components

import android.content.Context
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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCities: MutableList<String>,
    context: Context,
    viewModel: WeatherViewModel = hiltViewModel(),
    onDrawerClosed: @Composable () -> Unit
) {
    val cityNames = viewModel.getDistrictNames(context)
    if (drawerState.isClosed) {
        onDrawerClosed()
    }

    // Get screen width for responsive design
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    ModalDrawerSheet(
        modifier = Modifier
            .widthIn(min = 280.dp, max = screenWidth * 0.8f) // Responsive width, up to 80% of screen width
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Ara..") },
            modifier = Modifier
                .fillMaxWidth(0.9f) // 90% of drawer width
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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.9f) // 90% of drawer width
        ) {
            items(selectedCities) { cityName ->
                CheckboxList(cityName, selectedCities, context = context)
                HorizontalDivider(color = Color.Gray)
            }

            if (searchQuery.isNotEmpty()) {
                items(cityNames.filter {
                    it.contains(searchQuery, ignoreCase = true)
                }) { cityName ->
                    if (!selectedCities.contains(cityName)) {
                        CheckboxList(cityName, selectedCities, context = context)
                        HorizontalDivider(color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Created by hkarakaya",
            modifier = Modifier
                .padding(16.dp)
                .wrapContentWidth(), // Wrap content width for the text
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    }
}
