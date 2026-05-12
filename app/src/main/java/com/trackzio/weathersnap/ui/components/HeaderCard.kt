package com.trackzio.weathersnap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.trackzio.weathersnap.ui.theme.Mint100
import com.trackzio.weathersnap.ui.theme.Mint200
import com.trackzio.weathersnap.ui.theme.Olive850

@Composable
fun HeaderCard(
    title: String,
    subtitle: String,
    actionText: String?,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val backgroundBrush = Brush.horizontalGradient(listOf(Mint200.copy(alpha = 0.65f), Mint100.copy(alpha = 0.65f)))
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundBrush, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = Olive850)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Olive850.copy(alpha = 0.85f))
        }
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.width(12.dp))
            OutlinePillButton(
                text = actionText,
                onClick = onActionClick,
                contentColor = Olive850
            )
        }
    }
}
