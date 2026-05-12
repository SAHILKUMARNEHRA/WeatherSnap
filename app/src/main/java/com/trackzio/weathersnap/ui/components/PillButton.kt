package com.trackzio.weathersnap.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trackzio.weathersnap.ui.theme.CardBorder
import com.trackzio.weathersnap.ui.theme.Mint200
import com.trackzio.weathersnap.ui.theme.Olive950

@Composable
fun PrimaryPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(999.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Mint200,
            contentColor = Olive950,
            disabledContainerColor = Mint200.copy(alpha = 0.4f),
            disabledContentColor = Olive950.copy(alpha = 0.6f),
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun OutlinePillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(999.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
        border = BorderStroke(1.dp, CardBorder),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

