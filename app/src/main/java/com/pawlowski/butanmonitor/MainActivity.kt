package com.pawlowski.butanmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawlowski.butanmonitor.ui.screen.choosePeriod.ChoosePeriodScreen
import com.pawlowski.butanmonitor.ui.screen.main.MainDestination
import com.pawlowski.butanmonitor.ui.theme.ButanMonitorTheme
import dagger.hilt.android.AndroidEntryPoint

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
                                    navController.navigate(route = "History")
                                },
                            )
                        }
                        composable(route = "History") {
                        }
                    }
                }
            }
        }
    }
}
