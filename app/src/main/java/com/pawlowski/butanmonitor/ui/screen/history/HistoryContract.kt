package com.pawlowski.butanmonitor.ui.screen.history

import com.pawlowski.butanmonitor.utils.Resource
import com.pawlowski.network.domain.Measurement
import kotlinx.collections.immutable.ImmutableList

data class HistoryState(
    val measurements: Resource<ImmutableList<Measurement>>,
)

sealed interface HistoryEvent {
    data object RetryClick : HistoryEvent

    data object BackClick : HistoryEvent
}
