/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin, Artem Zaytsev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.notification.ui.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Помошник создания нотификации в системном трее
 */
object NotificationCreateHelper {

    fun showNotification(context: Context, pushHandleStrategy: PushHandleStrategy<*>,
                         title: String, body: String) {
        SdkUtils.runOnOreo {
            getNotificationManager(context).createNotificationChannel(
                    pushHandleStrategy.channel ?: buildChannel(pushHandleStrategy, title, body, context)
            )
        }

        val notificationBuilder = pushHandleStrategy.notificationBuilder
                ?: buildNotification(pushHandleStrategy, title, body, context)

        getNotificationManager(context).notify(title.hashCode(), notificationBuilder.build())
    }

    @SuppressLint("NewApi")
    private fun buildNotification(pushHandleStrategy: PushHandleStrategy<*>,
                                  title: String,
                                  body: String,
                                  context: Context): NotificationCompat.Builder =
            NotificationCompat.Builder(context, context.getString(pushHandleStrategy.channelId))
                    .setSmallIcon(pushHandleStrategy.icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(ContextCompat.getColor(context, pushHandleStrategy.color))
                    .setContent(pushHandleStrategy.contentView)
                    .setAutoCancel(pushHandleStrategy.autoCancelable)
                    .setContentIntent(pushHandleStrategy.pendingIntent)


    @SuppressLint("NewApi")
    private fun buildChannel(pushHandleStrategy: PushHandleStrategy<*>,
                             title: String,
                             body: String,
                             context: Context): NotificationChannel {
        val channel = NotificationChannel(
                context.getString(pushHandleStrategy.channelId),
                title,
                NotificationManager.IMPORTANCE_HIGH)

        channel.description = body
        channel.enableLights(true)
        channel.enableVibration(true)
        return channel
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}