package com.pawlowski.butanmonitor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DiModule {
    @Provides
    @Singleton
    fun httpClient(json: Json): HttpClient =
        HttpClient(CIO) {
            install(WebSockets) {
                pingInterval = 20_000
                contentConverter = KotlinxWebsocketSerializationConverter(json)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println(message) // TODO
                        }
                    }
            }
            install(ContentNegotiation) {
                json(json)
            }
        }

    @Singleton
    @Provides
    fun json(): Json =
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
}
