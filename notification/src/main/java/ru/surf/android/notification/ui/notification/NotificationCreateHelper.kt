package ru.surf.android.notification.ui.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.support.v4.app.NotificationCompat
import ru.surf.android.notification.R
import ru.surf.android.notification.interactor.push.BaseNotificationTypeData
import java.io.Serializable

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