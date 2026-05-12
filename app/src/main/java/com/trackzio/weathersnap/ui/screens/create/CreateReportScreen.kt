package com.trackzio.weathersnap.ui.screens.create

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trackzio.weathersnap.ui.components.AppBackground
import com.trackzio.weathersnap.ui.components.HeaderCard
import com.trackzio.weathersnap.ui.components.MetricRow
import com.trackzio.weathersnap.ui.components.PrimaryPillButton
import com.trackzio.weathersnap.ui.components.SurfaceCard
import com.trackzio.weathersnap.ui.components.TemperaturePill
import com.trackzio.weathersnap.ui.screens.draft.ReportDraftViewModel
import com.trackzio.weathersnap.ui.theme.CardBorder
import com.trackzio.weathersnap.ui.theme.Mint200
import com.trackzio.weathersnap.ui.util.findActivity
import com.trackzio.weathersnap.ui.util.formatSizeKb
import com.trackzio.weathersnap.ui.util.formatTemperature
import java.io.File

@Composable
fun CreateReportScreen(
    onBack: () -> Unit,
    onOpenCamera: () -> Unit,
    onSaved: () -> Unit,
    viewModel: CreateReportViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current.findActivity()
    val draftViewModel: ReportDraftViewModel = hiltViewModel(checkNotNull(activity))
    val draft by draftViewModel.state.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) {
            draftViewModel.clear()
            viewModel.consumeSaved()
            onSaved()
        }
    }

    AppBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                HeaderCard(
                    title = "Create Report",
                    subtitle = "Capture, compress, annotate",
                    actionText = "Back",
                    onActionClick = onBack
                )
            }

            item {
                val weather = draft.weather
                if (weather != null) {
                    SurfaceCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = weather.cityName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = weather.condition,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            TemperaturePill(text = formatTemperature(weather.temperatureC))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        MetricRow(
                            humidity = "${weather.humidityPercent}%",
                            wind = "${"%.2f".format(weather.windSpeedMs)} m/s",
                            pressure = "${weather.pressureHpa}"
                        )
                    }
                }
            }

            item {
                SurfaceCard {
                    val photo = draft.photo
                    val imageBitmap = remember(photo?.compressedPath) {
                        val path = photo?.compressedPath ?: return@remember null
                        val bitmap = BitmapFactory.decodeFile(path) ?: return@remember null
                        bitmap.asImageBitmap()
                    }

                    androidx.compose.material3.Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            val brush = Brush.horizontalGradient(
                                colors = listOf(Mint200.copy(alpha = 0.12f), Mint200.copy(alpha = 0.05f))
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(brush)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Photo preview",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryPillButton(
                        text = if (photo == null) "Capture Photo" else "Retake Photo",
                        onClick = onOpenCamera,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedVisibility(
                        visible = photo != null,
                        enter = fadeIn(tween(180)),
                        exit = fadeOut(tween(180))
                    ) {
                        val currentPhoto = photo ?: return@AnimatedVisibility
                        Column {
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
                                        text = formatSizeKb(currentPhoto.originalSizeBytes),
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
                                        text = formatSizeKb(currentPhoto.compressedSizeBytes),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Mint200
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                SurfaceCard {
                    Text(
                        text = "Field Notes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        value = draft.notes,
                        onValueChange = draftViewModel::setNotes,
                        label = { Text("Notes") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Mint200,
                            unfocusedBorderColor = CardBorder,
                            focusedLabelColor = Mint200,
                            cursorColor = Mint200,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                }
            }

            item {
                if (uiState.errorMessage != null) {
                    SurfaceCard {
                        Text(
                            text = uiState.errorMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            item {
                PrimaryPillButton(
                    text = if (uiState.isSaving) "Saving..." else "Save Report",
                    onClick = { viewModel.save(draft) },
                    enabled = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                AnimatedVisibility(
                    visible = uiState.isSaving,
                    enter = fadeIn(tween(180)),
                    exit = fadeOut(tween(180))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Mint200
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Saving report...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }
    }
}
