package com.pawlowski.butanmonitor.ui.screen.main

import com.pawlowski.butanmonitor.utils.Resource
import com.pawlowski.network.domain.Measurement
import kotlinx.collections.immutable.ImmutableList

data class MainState(
    val measurements: ImmutableList<Measurement>,
    val isLoading: Boolean,
    val isError: Boolean,
    val thresholdsRequestResource: Resource<Unit>?,
)

sealed interface MainEvent {
    data object RetryClick : MainEvent

    data class UpdateThresholds(
        val propaneThreshold: Int?,
        val ammoniaThreshold: Int?,
    ) : MainEvent
}
