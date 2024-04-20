package com.pawlowski.butanmonitor.data

import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDto(
    val ammoniaLevel: Long,
    val propaneLevel: Long,
)
