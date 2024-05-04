package com.pawlowski.butanmonitor.ui.screen.main

import com.pawlowski.butanmonitor.utils.Resource
import com.pawlowski.network.domain.Measurement
import com.pawlowski.network.domain.Thresholds
import kotlinx.collections.immutable.ImmutableList

data class MainState(
    val measurements: ImmutableList<Measurement>,
    val isLoading: Boolean,
    val isError: Boolean,
    val thresholdsRequestResource: Resource<Unit>?,
    val thresholdsResource: Resource<Thresholds>,
)

sealed interface MainEvent {
    data object RetryClick : MainEvent

    data class UpdateThresholds(
        val propaneThreshold: Int?,
        val ammoniaThreshold: Int?,
    ) : MainEvent
}
