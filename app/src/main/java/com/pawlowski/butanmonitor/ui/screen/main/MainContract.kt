package com.pawlowski.butanmonitor.ui.screen.main

import com.pawlowski.network.domain.Measurement
import kotlinx.collections.immutable.ImmutableList

data class MainState(
    val measurements: ImmutableList<Measurement>,
    val isLoading: Boolean,
    val isError: Boolean,
)

sealed interface MainEvent {
    data object RetryClick : MainEvent
}
