package com.pawlowski.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationTokenDto(
    @SerialName("token") val token: String,
)
