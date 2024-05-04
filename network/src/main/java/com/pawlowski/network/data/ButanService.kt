package com.pawlowski.network.data

import com.pawlowski.network.domain.Measurement
import com.pawlowski.network.domain.Thresholds
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.parameter
import io.ktor.client.request.port
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
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
                    while (true) {
                        val data = receiveDeserialized<MeasurementDto>()
                        emit(data)
                    }
                }
            }.map {
                it.toDomain()
            }

        suspend fun getHistoryMeasurements(
            from: Long,
            to: Long,
        ): ImmutableList<Measurement> =
            httpClient.request(urlString = "http://srv3.enteam.pl/measurements") {
                method = HttpMethod.Get
                port = 3012
                parameter("from", from)
                parameter("to", to)
            }.body<List<MeasurementDto>>()
                .map { it.toDomain() }
                .toPersistentList()

        suspend fun setNotificationToken(token: String) {
            httpClient.request(urlString = "http://srv3.enteam.pl/fcm") {
                method = HttpMethod.Put
                port = 3012
                contentType(type = ContentType.parse("application/json"))
                setBody(body = NotificationTokenDto(token = token))
            }.body<NotificationTokenDto>()
        }

        suspend fun updateThresholds(
            propaneThreshold: Int?,
            ammoniaThreshold: Int?,
        ) {
            httpClient.request(urlString = "http://srv3.enteam.pl/thresholds") {
                method = HttpMethod.Put
                port = 3012
                contentType(type = ContentType.parse("application/json"))
                setBody(
                    body =
                        ThresholdsDto(
                            propaneThreshold = propaneThreshold,
                            ammoniaThreshold = ammoniaThreshold,
                        ),
                )
            }.body<ThresholdsDto>()
        }

        suspend fun geThresholds(): Thresholds =
            httpClient.request(urlString = "http://srv3.enteam.pl/thresholds") {
                method = HttpMethod.Get
                port = 3012
                contentType(type = ContentType.parse("application/json"))
            }.body<ThresholdsDto>().let {
                Thresholds(
                    ammoniaThreshold = it.ammoniaThreshold,
                    propaneThreshold = it.propaneThreshold,
                )
            }

        private fun MeasurementDto.toDomain(): Measurement =
            Measurement(
                ammoniaLevel = ammoniaLevel,
                propaneLevel = propaneLevel,
                timestamp = Instant.parse(measuredAt).toEpochMilliseconds(),
            )
    }
