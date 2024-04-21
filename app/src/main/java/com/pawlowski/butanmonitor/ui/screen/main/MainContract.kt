package com.pawlowski.butanmonitor.ui.screen.main

import com.pawlowski.butanmonitor.domain.model.Measurement

data class MainState(
    val measurements: List<Measurement>,
    val isLoading: Boolean,
    val isError: Boolean,
)

sealed interface MainEvent {
    data object RetryClick : MainEvent
}
