package com.pawlowski.butanmonitor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawlowski.butanmonitor.data.MeasurementDto
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : ViewModel() {
        private val client =
            HttpClient(CIO) {
                install(WebSockets) {
                    pingInterval = 20_000
                    contentConverter = KotlinxWebsocketSerializationConverter(Json)
                }
            }

        init {

            viewModelScope.launch {
                println("MainViewModel: Init")
                runCatching {
                    client.webSocket(method = HttpMethod.Get, host = "srv3.enteam.pl", port = 3012, path = "/ws") {
                        println("MainViewModel: Connection active")
                        while (true) {
                            val data = receiveDeserialized<MeasurementDto>()
                            println(data)
                        }
                    }
                }.onFailure {
                    println("MainViewModel: Connection failure")
                    it.printStackTrace()
                }

                client.close()
            }
        }
    }
