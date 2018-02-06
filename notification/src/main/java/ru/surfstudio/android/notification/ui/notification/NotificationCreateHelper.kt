package ru.surfstudio.android.notification.ui.notification

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat

/**
 * Помошник создания нотификации в системном трее
 */
object NotificationCreateHelper {

    fun showNotification(context: Context, pushHandleStrategy: PushHandleStrategy<*>,
                         title: String, body: String) {
        val notificationBuilder = NotificationCompat.Builder(context, pushHandleStrategy.channelId)
                .setSmallIcon(pushHandleStrategy.icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(pushHandleStrategy.autoCancelable)
                .setContentIntent(pushHandleStrategy.pendingIntent)
        getNotificationManager(context).notify(title.hashCode(), notificationBuilder.build())
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}