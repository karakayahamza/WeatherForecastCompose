package com.example.weatherforecastcompose.ui.components

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
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
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search cities") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                focusedBorderColor = Color.Blue, // Customize focused border color
//                unfocusedBorderColor = Color.Gray, // Customize unfocused border color
//                cursorColor = Color.Black // Customize cursor color
//            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Perform search action here
                    keyboardController?.hide() // Hide keyboard when search action is performed
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