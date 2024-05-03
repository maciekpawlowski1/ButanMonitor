package com.pawlowski.butanmonitor

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.pawlowski.butanmonitor.ui.screen.choosePeriod.ChoosePeriodScreen
import com.pawlowski.butanmonitor.ui.screen.history.HistoryDestination
import com.pawlowski.butanmonitor.ui.screen.main.MainDestination
import com.pawlowski.butanmonitor.ui.theme.ButanMonitorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ButanMonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val permissionLauncher =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            null
                        }

                    LaunchedEffect(key1 = permissionLauncher?.status?.isGranted) {
                        if (permissionLauncher?.status?.isGranted == false) {
                            permissionLauncher.launchPermissionRequest()
                        }
                    }

                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "Main") {
                        composable(route = "Main") {
                            MainDestination(
                                onNavigateToChoosePeriod = {
                                    navController.navigate(route = "ChoosePeriod")
                                },
                            )
                        }
                        composable(route = "ChoosePeriod") {
                            ChoosePeriodScreen(
                                onConfirmClick = { from, to ->
                                    navController.navigate(
                                        route = "History/${
                                            from.toInstant(timeZone = TimeZone.currentSystemDefault()).epochSeconds
                                        }/${
                                            to.toInstant(timeZone = TimeZone.currentSystemDefault()).epochSeconds
                                        }",
                                    )
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                },
                            )
                        }
                        composable(
                            route = "History/{from}/{to}",
                            arguments =
                                listOf(
                                    navArgument("from") {
                                        type = NavType.LongType
                                    },
                                    navArgument("to") {
                                        type = NavType.LongType
                                    },
                                ),
                        ) {
                            HistoryDestination(
                                onNavigateBack = navController::popBackStack,
                            )
                        }
                    }
                }
            }
        }
    }
}
