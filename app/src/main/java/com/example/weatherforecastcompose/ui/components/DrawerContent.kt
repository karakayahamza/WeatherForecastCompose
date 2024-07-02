package com.example.weatherforecastcompose.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    cityNames: List<String>,
    selectedCities: MutableList<String>,
    context: Context
) {
    ModalDrawerSheet(
        Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search cities") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            // Show selected cities regardless of search query
            items(selectedCities) { cityName ->
                CheckboxList(cityName, selectedCities, context = context)
                HorizontalDivider(color = Color.Gray)
            }

            // Show filtered cities based on search query
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