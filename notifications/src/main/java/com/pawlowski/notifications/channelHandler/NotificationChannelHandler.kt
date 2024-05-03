package com.pawlowski.notifications.channelHandler

import android.app.NotificationChannel
import android.app.NotificationManager
import javax.inject.Inject
import javax.inject.Singleton

const val NOTIFICATION_CHANNEL_ID = "ReadingMessage"

@Singleton
internal class NotificationChannelHandler
    @Inject
    constructor(
        private val notificationManager: NotificationManager,
    ) : INotificationChannelHandler {
        private val channels by lazy {
            listOf(
                MyNotificationChannel(
                    channelId = NOTIFICATION_CHANNEL_ID,
                    tittle = "Alert",
                    description = "Przychodzi powiadomienie gdy dostajemy alert",
                ),
            )
        }

        override fun innitNotificationChannels() {
            channels.forEach {
                val name = it.tittle
                val descriptionText = it.description
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel =
                    NotificationChannel(it.channelId, name, importance).apply {
                        description = descriptionText
                    }
                // Register the channel with the system
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
