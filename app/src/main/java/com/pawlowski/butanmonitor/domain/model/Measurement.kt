package com.pawlowski.butanmonitor.domain.model

data class Measurement(
    val ammoniaLevel: Int,
    val propaneLevel: Int,
    val timestamp: Long,
)
