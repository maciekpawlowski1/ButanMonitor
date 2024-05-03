package com.pawlowski.network.domain

import androidx.compose.runtime.Immutable

@Immutable
data class Measurement(
    val ammoniaLevel: Int,
    val propaneLevel: Int,
    val timestamp: Long,
)
