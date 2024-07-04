package com.example.weatherforecastcompose

import WeatherMainScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme
import com.example.weatherforecastcompose.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)




        setContent {
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.black)

            val viewModel: WeatherViewModel = hiltViewModel()
            WeatherForecastComposeTheme {

                WeatherMainScreen(viewModel)
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewMain() {
}




