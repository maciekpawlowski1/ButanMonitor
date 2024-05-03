package com.pawlowski.butanmonitor.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.butanmonitor.utils.RetrySharedFlow
import com.pawlowski.network.data.ButanService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val butanService: ButanService,
    ) : ViewModel() {
        private val retrySharedFlow = RetrySharedFlow()

        val stateFlow
            get() = _stateFlow.asStateFlow()

        private val _stateFlow =
            MutableStateFlow(
                MainState(
                    measurements = persistentListOf(),
                    isError = false,
                    isLoading = true,
                ),
            )

        val subscriptionFlow by lazy {
            butanService.getLiveMeasurements()
                .onEach { newMeasurement ->
                    _stateFlow.update {
                        it.copy(
                            measurements = (it.measurements + newMeasurement).toPersistentList(),
                            isLoading = false,
                        )
                    }
                    println(newMeasurement)
                }
                .retryWhen { cause, _ ->
                    cause.printStackTrace()
                    _stateFlow.update {
                        it.copy(
                            isError = true,
                            isLoading = false,
                        )
                    }
                    retrySharedFlow.waitForRetry()
                    _stateFlow.update {
                        it.copy(
                            isError = false,
                            isLoading = true,
                        )
                    }
                    true
                }.map {}
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                )
        }

        fun onNewEvent(event: MainEvent) {
            when (event) {
                is MainEvent.RetryClick -> {
                    retrySharedFlow.sendRetryEvent()
                }
            }
        }
    }
