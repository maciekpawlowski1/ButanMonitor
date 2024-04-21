package com.pawlowski.butanmonitor.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDto(
    @SerialName("AmmoniaLevel") val ammoniaLevel: Int,
    @SerialName("PropaneLevel") val propaneLevel: Int,
    @SerialName("MeasuredAt") val measuredAt: String,
)
