package com.pawlowski.butanmonitor.data

import com.pawlowski.butanmonitor.domain.model.Measurement
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import javax.inject.Inject

class ButanService
    @Inject
    constructor(
        private val httpClient: HttpClient,
    ) {
        fun getLiveMeasurements(): Flow<Measurement> =
            flow {
                httpClient.webSocket(method = HttpMethod.Get, host = "srv3.enteam.pl", port = 3012, path = "/ws") {
                    println("MainViewModel: Connection active")
                    while (true) {
                        val data = receiveDeserialized<MeasurementDto>()
                        emit(data)
                    }
                }
            }.map {
                it.toDomain()
            }

        private fun MeasurementDto.toDomain(): Measurement =
            Measurement(
                ammoniaLevel = ammoniaLevel,
                propaneLevel = propaneLevel,
                timestamp = Instant.parse(measuredAt).toEpochMilliseconds(),
            )
    }
