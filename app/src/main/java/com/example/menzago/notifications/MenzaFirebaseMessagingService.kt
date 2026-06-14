package com.example.menzago.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MenzaFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "MenzaGO"
        val body = message.notification?.body ?: "Nova obavijest"

        MenzaNotificationHelper.showNotification(
            context = applicationContext,
            title = title,
            message = body
        )
    }
}