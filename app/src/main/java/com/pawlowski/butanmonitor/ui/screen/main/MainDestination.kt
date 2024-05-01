package com.pawlowski.butanmonitor.ui.screen.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MainDestination(onNavigateToChoosePeriod: () -> Unit) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    mainViewModel.subscriptionFlow.collectAsStateWithLifecycle(initialValue = Unit)

    val state = mainViewModel.stateFlow.collectAsStateWithLifecycle().value
    MainScreen(
        state = state,
        onHistoryClick = onNavigateToChoosePeriod,
        onEvent = mainViewModel::onNewEvent,
    )
}
