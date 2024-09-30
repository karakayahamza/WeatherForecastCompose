package com.example.weatherforecastcompose.presentation.compenents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign


@Composable
fun ErrorMessage(error: String) {
    Text(
        text = error,
        textAlign = TextAlign.Center
    )
}
