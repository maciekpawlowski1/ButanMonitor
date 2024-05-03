package com.pawlowski.notifications.di

import android.app.NotificationManager
import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import com.pawlowski.notifications.channelHandler.INotificationChannelHandler
import com.pawlowski.notifications.channelHandler.NotificationChannelHandler
import com.pawlowski.notifications.synchronization.INotificationTokenSynchronizer
import com.pawlowski.notifications.synchronization.NotificationTokenSynchronizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NotificationServiceModule {
    @Provides
    internal fun synchronizer(notificationTokenSynchronizer: NotificationTokenSynchronizer): INotificationTokenSynchronizer =
        notificationTokenSynchronizer

    @Singleton
    @Provides
    fun firebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun notificationManager(appContext: Context): NotificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    internal fun notificationChannelHandler(notificationChannelHandler: NotificationChannelHandler): INotificationChannelHandler =
        notificationChannelHandler
}
