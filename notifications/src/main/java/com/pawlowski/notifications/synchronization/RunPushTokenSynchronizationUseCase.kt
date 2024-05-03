package com.pawlowski.notifications.synchronization

import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.notifications.channelHandler.INotificationChannelHandler
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class RunPushTokenSynchronizationUseCase
    @Inject
    constructor(
        private val notificationsSynchronizer: INotificationTokenSynchronizer,
        private val firebaseMessaging: FirebaseMessaging,
        private val notificationChannelHandler: INotificationChannelHandler,
    ) {
        suspend operator fun invoke() {
            runCatching {
                val token = firebaseMessaging.token.await().also { println(it) }
                notificationsSynchronizer.synchronizeWithServer(newToken = token)
                notificationChannelHandler.innitNotificationChannels()
            }.onFailure {
                coroutineContext.ensureActive()
                it.printStackTrace()
            }
        }
    }
