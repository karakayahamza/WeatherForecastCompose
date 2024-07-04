package com.example.weatherforecastcompose.view.components

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    cityNames: List<String>,
    selectedCities: MutableList<String>,
    context: Context,
    onDrawerClosed: @Composable () -> Unit
) {
    if (drawerState.isClosed) {
        onDrawerClosed()
    }

    ModalDrawerSheet(
        Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
    ) {

        Spacer(modifier = Modifier.height(16.dp))
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Ara..") },
            modifier = Modifier
                .fillMaxWidth()
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

        LazyColumn {
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

        Spacer(modifier = Modifier.height(16.dp))
    }
}