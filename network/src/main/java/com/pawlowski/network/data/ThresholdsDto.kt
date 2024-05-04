package com.pawlowski.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThresholdsDto(
    @SerialName("ammoniaThreshold") val ammoniaThreshold: Int?,
    @SerialName("propaneThreshold") val propaneThreshold: Int?,
)
