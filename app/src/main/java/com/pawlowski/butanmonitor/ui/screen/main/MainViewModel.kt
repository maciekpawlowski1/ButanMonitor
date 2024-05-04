package com.pawlowski.butanmonitor.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.butanmonitor.utils.Resource
import com.pawlowski.butanmonitor.utils.RetrySharedFlow
import com.pawlowski.butanmonitor.utils.resourceFlowWithRetrying
import com.pawlowski.network.data.ButanService
import com.pawlowski.network.domain.Thresholds
import com.pawlowski.notifications.synchronization.RunPushTokenSynchronizationUseCase
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val butanService: ButanService,
        private val runPushTokenSynchronizationUseCase: RunPushTokenSynchronizationUseCase,
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
                    thresholdsRequestResource = null,
                    thresholdsResource = Resource.Loading,
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

                is MainEvent.UpdateThresholds -> {
                    if (stateFlow.value.thresholdsRequestResource == null) {
                        viewModelScope.launch {
                            resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                                butanService.updateThresholds(
                                    propaneThreshold = event.propaneThreshold,
                                    ammoniaThreshold = event.ammoniaThreshold,
                                )
                            }.collect { newResource ->
                                _stateFlow.update {
                                    val newValueOrNull =
                                        if (newResource is Resource.Success) {
                                            null
                                        } else {
                                            newResource
                                        }

                                    it.copy(
                                        thresholdsRequestResource = newValueOrNull,
                                        thresholdsResource =
                                            Resource.Success(
                                                Thresholds(
                                                    ammoniaThreshold = event.ammoniaThreshold,
                                                    propaneThreshold = event.propaneThreshold,
                                                ),
                                            ),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        init {
            viewModelScope.launch {
                runPushTokenSynchronizationUseCase()
            }
            viewModelScope.launch {
                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                    butanService.getThresholds()
                }.collect { newThresholds ->
                    _stateFlow.update {
                        it.copy(thresholdsResource = newThresholds)
                    }
                }
            }
        }
    }
