package com.pawlowski.butanmonitor.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pawlowski.butanmonitor.ui.components.error.ErrorItem
import com.pawlowski.butanmonitor.ui.screen.chooseTresholdsBottomSheet.ChooseThresholdsBottomSheet
import com.pawlowski.butanmonitor.ui.screen.main.chart.LiveChart
import com.pawlowski.butanmonitor.utils.Resource

@Composable
fun MainScreen(
    state: MainState,
    onEvent: (MainEvent) -> Unit,
    onHistoryClick: () -> Unit,
) {
    Column {
        val showThresholdsBottomSheet =
            remember {
                mutableStateOf(false)
            }
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            IconButton(
                onClick = onHistoryClick,
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = { showThresholdsBottomSheet.value = true },
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                )
            }
        }

        ChooseThresholdsBottomSheet(
            show = showThresholdsBottomSheet.value,
            onDismiss = { showThresholdsBottomSheet.value = false },
            onConfirm = { ammoniaThreshold, propaneThreshold ->
                onEvent(
                    MainEvent.UpdateThresholds(
                        propaneThreshold = propaneThreshold,
                        ammoniaThreshold = ammoniaThreshold,
                    ),
                )
                showThresholdsBottomSheet.value = false
            },
        )

        when {
            state.isLoading || state.thresholdsRequestResource is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            state.isError || state.thresholdsRequestResource is Resource.Error -> {
                ErrorItem(
                    onRetryClick = { onEvent(MainEvent.RetryClick) },
                )
            }
            else -> {
                LiveChart(measurements = state.measurements)
            }
        }
    }
}
