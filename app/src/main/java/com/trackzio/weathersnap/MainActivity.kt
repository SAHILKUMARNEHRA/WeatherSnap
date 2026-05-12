package com.trackzio.weathersnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.trackzio.weathersnap.ui.nav.WeatherSnapNavHost
import com.trackzio.weathersnap.ui.theme.WeatherSnapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            WeatherSnapTheme {
                WeatherSnapNavHost()
            }
        }
    }
}

