package com.trackzio.weathersnap.ui.screens.reports

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trackzio.weathersnap.domain.Report
import com.trackzio.weathersnap.ui.components.AppBackground
import com.trackzio.weathersnap.ui.components.HeaderCard
import com.trackzio.weathersnap.ui.components.SurfaceCard
import com.trackzio.weathersnap.ui.components.TemperaturePill
import com.trackzio.weathersnap.ui.theme.Mint200
import com.trackzio.weathersnap.ui.util.formatSizeKb
import com.trackzio.weathersnap.ui.util.formatTemperature
import com.trackzio.weathersnap.ui.util.formatTimestamp

@Composable
fun SavedReportsScreen(
    onBack: () -> Unit,
    viewModel: SavedReportsViewModel = hiltViewModel(),
) {
    val reports by viewModel.reports.collectAsState()

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HeaderCard(
                title = "Saved Reports",
                subtitle = "${reports.size} report stored locally",
                actionText = "Back",
                onActionClick = onBack
            )

            if (reports.isEmpty()) {
                SurfaceCard {
                    Text(
                        text = "No reports yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Create a report from the weather screen.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reports) { report ->
                        ReportCard(report = report)
                    }
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ReportCard(report: Report) {
    SurfaceCard {
        val image = remember(report.compressedImagePath) {
            val bitmap = BitmapFactory.decodeFile(report.compressedImagePath) ?: return@remember null
            bitmap.asImageBitmap()
        }

        if (image != null) {
            androidx.compose.material3.Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
            ) {
                Image(
                    bitmap = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = report.cityName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = report.condition,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
                Text(
                    text = formatTimestamp(report.createdAtEpochMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            TemperaturePill(text = formatTemperature(report.temperatureC))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SurfaceCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 14.dp,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
            ) {
                Text(
                    text = "Original",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
                Text(
                    text = formatSizeKb(report.originalSizeBytes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Mint200
                )
            }
            SurfaceCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 14.dp,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
            ) {
                Text(
                    text = "Compressed",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
                Text(
                    text = formatSizeKb(report.compressedSizeBytes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Mint200
                )
            }
        }

        if (report.notes.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            SurfaceCard(
                cornerRadius = 14.dp,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
            ) {
                Text(
                    text = report.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }
    }
}
