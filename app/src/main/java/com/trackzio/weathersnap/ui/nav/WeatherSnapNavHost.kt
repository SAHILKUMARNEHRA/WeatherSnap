package com.trackzio.weathersnap.ui.nav

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trackzio.weathersnap.ui.screens.camera.CameraScreen
import com.trackzio.weathersnap.ui.screens.create.CreateReportScreen
import com.trackzio.weathersnap.ui.screens.reports.SavedReportsScreen
import com.trackzio.weathersnap.ui.screens.weather.WeatherScreen

object Routes {
    const val Root = "root"
    const val Weather = "weather"
    const val CreateReport = "create_report"
    const val Camera = "camera"
    const val SavedReports = "saved_reports"
}

@Composable
fun WeatherSnapNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.safeDrawing.union(WindowInsets.ime)
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Routes.Weather,
            route = Routes.Root
        ) {
            composable(Routes.Weather) {
                WeatherScreen(
                    onOpenReports = { navController.navigate(Routes.SavedReports) },
                    onCreateReport = { navController.navigate(Routes.CreateReport) }
                )
            }
            composable(Routes.CreateReport) {
                CreateReportScreen(
                    onBack = { navController.popBackStack() },
                    onOpenCamera = { navController.navigate(Routes.Camera) },
                    onSaved = {
                        navController.navigate(Routes.SavedReports) {
                            popUpTo(Routes.Weather)
                        }
                    }
                )
            }
            composable(Routes.Camera) {
                CameraScreen(
                    onClose = { navController.popBackStack() }
                )
            }
            composable(Routes.SavedReports) {
                SavedReportsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
