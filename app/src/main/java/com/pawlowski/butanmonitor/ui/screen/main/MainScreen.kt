package com.pawlowski.butanmonitor.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pawlowski.butanmonitor.ui.components.error.ErrorItem
import com.pawlowski.butanmonitor.ui.screen.main.chart.LiveChart

@Composable
fun MainScreen(
    state: MainState,
    onEvent: (MainEvent) -> Unit,
    onHistoryClick: () -> Unit,
) {
    Column {
        IconButton(
            onClick = onHistoryClick,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = null,
            )
        }
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            state.isError -> {
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
