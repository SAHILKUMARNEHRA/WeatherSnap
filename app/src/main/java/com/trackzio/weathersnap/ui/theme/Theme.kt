package com.trackzio.weathersnap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Mint200,
    onPrimary = Olive950,
    secondary = Mint100,
    onSecondary = Olive950,
    background = Olive950,
    onBackground = Mint100,
    surface = Olive900,
    onSurface = Mint100,
)

@Composable
fun WeatherSnapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

