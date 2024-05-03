package com.pawlowski.notifications.synchronization

import android.util.Log
import com.pawlowski.network.data.ButanService
import kotlinx.coroutines.ensureActive
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
internal class NotificationTokenSynchronizer
    @Inject
    constructor(
        private val notificationsDataProvider: ButanService,
    ) : INotificationTokenSynchronizer {
        override suspend fun synchronizeWithServer(newToken: String) {
            runCatching {
                notificationsDataProvider.setNotificationToken(
                    token = newToken,
                )
            }.onFailure {
                coroutineContext.ensureActive()
                Log.d("NotificationTokenSynchronizer", "Error")
            }.onSuccess {
                Log.d("NotificationTokenSynchronizer", "Success")
            }
        }
    }
