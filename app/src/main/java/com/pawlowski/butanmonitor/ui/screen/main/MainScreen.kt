package com.pawlowski.butanmonitor.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pawlowski.butanmonitor.ui.screen.main.chart.LiveChart

@Composable
fun MainScreen(
    state: MainState,
    onEvent: (MainEvent) -> Unit,
    onHistoryClick: () -> Unit,
) {
    Column {
        IconButton(onClick = onHistoryClick) {
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Something went wrong",
                        textAlign = TextAlign.Center,
                    )
                    Button(onClick = { onEvent(MainEvent.RetryClick) }) {
                        Text(text = "Reload")
                    }
                }
            }
            else -> {
                LiveChart(measurements = state.measurements)
            }
        }
    }
}
