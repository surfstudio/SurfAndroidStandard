package ru.surfstudio.android.notification.ui.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Помошник создания нотификации в системном трее
 */
object NotificationCreateHelper {

    fun showNotification(context: Context, pushHandleStrategy: PushHandleStrategy<*>,
                         title: String, body: String) {

        val notificationBuilder = buildNotification(pushHandleStrategy, title, body, context)
        getNotificationManager(context).notify(title.hashCode(), notificationBuilder.build())
    }

    @SuppressLint("NewApi")
    private fun buildNotification(pushHandleStrategy: PushHandleStrategy<*>,
                                  title: String,
                                  body: String,
                                  context: Context): NotificationCompat.Builder {
        if (SdkUtils.isAtLeastOreo) {
            buildChannel(pushHandleStrategy, title, body, context)
        }

        return NotificationCompat.Builder(context, pushHandleStrategy.channelId)
                .setSmallIcon(pushHandleStrategy.icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(pushHandleStrategy.autoCancelable)
                .setContentIntent(pushHandleStrategy.pendingIntent)
    }

    @SuppressLint("NewApi")
    private fun buildChannel(pushHandleStrategy: PushHandleStrategy<*>, title: String, body: String, context: Context) {
        val channel = NotificationChannel(pushHandleStrategy.channelId, title, NotificationManager.IMPORTANCE_HIGH)
        channel.description = body
        channel.enableLights(true)
        channel.enableVibration(true)
        getNotificationManager(context).createNotificationChannel(channel)
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}