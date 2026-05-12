package com.trackzio.weathersnap.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trackzio.weathersnap.ui.theme.BlueAccent
import com.trackzio.weathersnap.ui.theme.CardBorder
import com.trackzio.weathersnap.ui.theme.CardSurface
import com.trackzio.weathersnap.ui.theme.Mint200
import com.trackzio.weathersnap.ui.theme.OrangeAccent

@Composable
fun TemperaturePill(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Mint200.copy(alpha = 0.25f),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        content = {
            Text(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = Mint200
            )
        }
    )
}

@Composable
fun MetricRow(
    humidity: String,
    wind: String,
    pressure: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MetricCard(title = "Humidity", value = humidity, valueColor = Mint200, modifier = Modifier.weight(1f))
        MetricCard(title = "Wind", value = wind, valueColor = BlueAccent, modifier = Modifier.weight(1f))
        MetricCard(title = "Pressure", value = pressure, valueColor = OrangeAccent, modifier = Modifier.weight(1f))
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = CardSurface.copy(alpha = 0.8f),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            Text(text = value, style = MaterialTheme.typography.titleMedium, color = valueColor)
        }
    }
}
