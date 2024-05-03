package com.pawlowski.butanmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pawlowski.butanmonitor.ui.screen.choosePeriod.ChoosePeriodScreen
import com.pawlowski.butanmonitor.ui.screen.history.HistoryDestination
import com.pawlowski.butanmonitor.ui.screen.main.MainDestination
import com.pawlowski.butanmonitor.ui.theme.ButanMonitorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ButanMonitorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
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
