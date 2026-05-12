package com.trackzio.weathersnap.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.trackzio.weathersnap.ui.theme.CardBorder
import com.trackzio.weathersnap.ui.theme.CardSurface

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        color = CardSurface,
        shape = RoundedCornerShape(cornerRadius),
        border = BorderStroke(1.dp, CardBorder),
        content = {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.padding(contentPadding),
                content = content
            )
        }
    )
}

