package com.pawlowski.butanmonitor.ui.screen.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.butanmonitor.data.ButanService
import com.pawlowski.butanmonitor.utils.Resource
import com.pawlowski.butanmonitor.utils.RetrySharedFlow
import com.pawlowski.butanmonitor.utils.resourceFlowWithRetrying
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val butanService: ButanService,
    ) : ViewModel() {
        private val from = savedStateHandle.get<Long>("from")!!
        private val to = savedStateHandle.get<Long>("to")!!

        private val retrySharedFlow = RetrySharedFlow()

        val measurements by lazy {
            resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                butanService.getHistoryMeasurements(
                    from = from,
                    to = to,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = Resource.Loading,
            )
        }

        fun onRetryClick() = retrySharedFlow.sendRetryEvent()
    }
