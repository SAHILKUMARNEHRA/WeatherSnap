package com.trackzio.weathersnap.ui.screens.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trackzio.weathersnap.domain.CitySuggestion
import com.trackzio.weathersnap.ui.components.AppBackground
import com.trackzio.weathersnap.ui.components.HeaderCard
import com.trackzio.weathersnap.ui.components.MetricRow
import com.trackzio.weathersnap.ui.components.PrimaryPillButton
import com.trackzio.weathersnap.ui.components.SurfaceCard
import com.trackzio.weathersnap.ui.components.TemperaturePill
import com.trackzio.weathersnap.ui.screens.draft.ReportDraftViewModel
import com.trackzio.weathersnap.ui.theme.CardBorder
import com.trackzio.weathersnap.ui.theme.Mint200
import com.trackzio.weathersnap.ui.theme.Olive850
import com.trackzio.weathersnap.ui.util.findActivity
import com.trackzio.weathersnap.ui.util.formatTemperature

@Composable
fun WeatherScreen(
    onOpenReports: () -> Unit,
    onCreateReport: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val activity = LocalContext.current.findActivity()
    val draftViewModel: ReportDraftViewModel = hiltViewModel(checkNotNull(activity))

    AppBackground {
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HeaderCard(
                title = "WeatherSnap",
                subtitle = "Live weather reports with camera evidence",
                actionText = "Reports",
                onActionClick = onOpenReports
            )

            SurfaceCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = state.query,
                        onValueChange = viewModel::onQueryChange,
                        singleLine = true,
                        label = { Text("City") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Mint200,
                            unfocusedBorderColor = CardBorder,
                            focusedLabelColor = Mint200,
                            cursorColor = Mint200,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    PrimaryPillButton(
                        text = "Search",
                        onClick = viewModel::onSearchClick,
                        enabled = state.selectedCity != null && !state.isLoadingWeather
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = state.helperText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                AnimatedVisibility(
                    visible = state.isLoadingSuggestions,
                    enter = fadeIn(tween(180)),
                    exit = fadeOut(tween(180))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Mint200
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Finding cities...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.suggestions.isNotEmpty(),
                enter = fadeIn(tween(180)) + slideInVertically(tween(180)) { it / 4 },
                exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { it / 4 },
            ) {
                SuggestionsCard(
                    suggestions = state.suggestions,
                    onSelect = viewModel::onSelectSuggestion
                )
            }

            WeatherResultCard(
                state = state,
                onCreateReport = {
                    val weather = state.weather ?: return@WeatherResultCard
                    draftViewModel.setWeather(weather)
                    onCreateReport()
                }
            )
        }
    }
}

@Composable
private fun SuggestionsCard(
    suggestions: List<CitySuggestion>,
    onSelect: (CitySuggestion) -> Unit,
) {
    SurfaceCard(contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(suggestions) { item ->
                androidx.compose.material3.Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(item) },
                    shape = RoundedCornerShape(999.dp),
                    color = Olive850.copy(alpha = 0.25f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        text = item.displayDetail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherResultCard(
    state: WeatherUiState,
    onCreateReport: () -> Unit,
) {
    SurfaceCard {
        if (state.isLoadingWeather) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = Mint200
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Loading weather...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
            return@SurfaceCard
        }

        val weather = state.weather
        if (weather == null) {
            Text(
                text = state.errorMessage ?: "No weather loaded",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter more than 2 letters, choose a city, then search.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            return@SurfaceCard
        }

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

        Spacer(modifier = Modifier.height(12.dp))
        SurfaceCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 14.dp,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Report readiness",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
                Text(
                    text = "Camera and Room DB enabled",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        PrimaryPillButton(
            text = "Create Report",
            onClick = onCreateReport,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
