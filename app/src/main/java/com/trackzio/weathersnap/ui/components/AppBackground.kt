package com.trackzio.weathersnap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.trackzio.weathersnap.ui.theme.Olive850
import com.trackzio.weathersnap.ui.theme.Olive950

@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val brush = Brush.verticalGradient(
        colors = listOf(Olive950, Olive850, Olive950)
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush),
        content = content
    )
}

