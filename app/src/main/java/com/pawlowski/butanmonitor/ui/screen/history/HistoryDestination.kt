package com.pawlowski.butanmonitor.ui.screen.history

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HistoryDestination(onNavigateBack: () -> Unit) {
    val viewModel = hiltViewModel<HistoryViewModel>()
    HistoryScreen(
        measurements = viewModel.measurements.collectAsStateWithLifecycle().value,
        onBackClick = onNavigateBack,
        onRetryClick = viewModel::onRetryClick,
    )
}
