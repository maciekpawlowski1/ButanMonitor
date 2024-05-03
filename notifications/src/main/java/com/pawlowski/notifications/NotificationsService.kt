package com.pawlowski.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pawlowski.notifications.channelHandler.NOTIFICATION_CHANNEL_ID
import kotlin.random.Random

class NotificationsService : FirebaseMessagingService() {
    /**
     * Called when message is received and app is in the foreground or app is in the background and user clicks it.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        Log.d(TAG, "Message data payload: " + remoteMessage.data)

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(
                appContext = applicationContext,
                tittle = it.title ?: "Przyszło nowe powiadomienie w aplikacji!",
                body = it.body ?: "Kliknij aby sprawdzić szczegóły",
                channelId = it.channelId ?: NOTIFICATION_CHANNEL_ID,
            )
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    private fun sendNotification(
        appContext: Context,
        tittle: String,
        body: String,
        channelId: String,
    ) {
        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java,
            ) as NotificationManager

        val intent =
            Intent(appContext, Class.forName("com.pawlowski.butanmonitor.MainActivity"))
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(appContext, channelId)
                .setContentTitle(tittle)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(Random.nextInt(), notification)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}
