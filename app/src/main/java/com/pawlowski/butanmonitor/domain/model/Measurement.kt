package com.pawlowski.butanmonitor.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Measurement(
    val ammoniaLevel: Int,
    val propaneLevel: Int,
    val timestamp: Long,
)
