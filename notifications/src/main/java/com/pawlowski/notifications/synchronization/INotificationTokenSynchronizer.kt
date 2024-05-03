package com.pawlowski.notifications.synchronization

interface INotificationTokenSynchronizer {
    suspend fun synchronizeWithServer(newToken: String)
}
