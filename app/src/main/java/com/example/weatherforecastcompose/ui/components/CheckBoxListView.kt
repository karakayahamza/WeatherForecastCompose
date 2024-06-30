package com.example.weatherforecastcompose.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.repository.saveSelectedCities


@Composable
fun CheckboxList(item: String, selectedItems: MutableList<String>, context: Context) {
    val isChecked = remember(item) {
        mutableStateOf(selectedItems.contains(item))
    }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                isChecked.value = !isChecked.value
                if (isChecked.value) {
                    selectedItems.add(0, item)
                } else {
                    selectedItems.remove(item)
                }
                saveSelectedCities(selectedItems, context)
            }) {
        Checkbox(
            checked = isChecked.value, onCheckedChange = {
                isChecked.value = it
                if (isChecked.value) {
                    selectedItems.add(0, item)
                } else {
                    selectedItems.remove(item)
                }
                saveSelectedCities(selectedItems, context)
            }, modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = item, fontSize = 16.sp, modifier = Modifier.fillMaxWidth(), color = Color.White
        )
    }
}