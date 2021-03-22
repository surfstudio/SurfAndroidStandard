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
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Помощник создания нотификации в системном трее
 */
object NotificationCreateHelper {

    fun showNotification(
            context: Context,
            pushHandleStrategy: PushHandleStrategy<*>,
            pushId: Int,
            title: String,
            body: String
    ) {
        val notificationBuilder = pushHandleStrategy.notificationBuilder
                ?: buildNotification(pushHandleStrategy, title, body, context)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

            pushHandleStrategy.group?.id?.let {
                makeGroupNotificationM(context, notificationBuilder, pushHandleStrategy, it, body, title)
                return
            }
        } else {
            SdkUtils.runOnOreo {
                getNotificationManager(context).createNotificationChannel(
                        pushHandleStrategy.channel
                                ?: buildChannel(pushHandleStrategy, body, context)
                )
            }

            //создание заголовка группы нотификаций происходит вручную
            pushHandleStrategy.group?.let {
                getNotificationManager(context)
                        .notify(it.id, pushHandleStrategy.groupSummaryNotificationBuilder?.build())

                Logger.i("active notifications: " + getNotificationManager(context).activeNotifications.map { it.id }.toList())
            }
        }

        getNotificationManager(context).notify(pushId, notificationBuilder.build())
    }

    @Deprecated("Используйте метод с pushId",
            ReplaceWith("showNotification(context, pushHandleStrategy, pushId, title, body"))
    fun showNotification(
            context: Context,
            pushHandleStrategy: PushHandleStrategy<*>,
            title: String,
            body: String
    ) {
        SdkUtils.runOnOreo {
            getNotificationManager(context).createNotificationChannel(
                    pushHandleStrategy.channel
                            ?: buildChannel(pushHandleStrategy, body, context)
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
                                  context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, context.getString(pushHandleStrategy.channelId))
                .setSmallIcon(pushHandleStrategy.icon)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(ContextCompat.getColor(context, pushHandleStrategy.color))
                .setContent(pushHandleStrategy.contentView)
                .setAutoCancel(pushHandleStrategy.autoCancelable)
                .setContentIntent(pushHandleStrategy.pendingIntent)
                .setDeleteIntent(pushHandleStrategy.deleteIntent)
    }

    @SuppressLint("NewApi")
    private fun buildChannel(pushHandleStrategy: PushHandleStrategy<*>,
                             body: String,
                             context: Context): NotificationChannel {
        val channel = NotificationChannel(
                context.getString(pushHandleStrategy.channelId),
                context.getString(pushHandleStrategy.channelName),
                NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = body
        channel.enableLights(true)
        channel.enableVibration(true)
        return channel
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun makeGroupNotificationM(
            context: Context,
            notificationBuilder: NotificationCompat.Builder,
            pushHandleStrategy: PushHandleStrategy<*>,
            groupId: Int,
            body: String,
            title: String
    ) {
        val notificationDescObject = NotificationGroupHelper
                .getNotificationsForGroup(context, groupId, body)

        if (notificationDescObject.size > 1) {

            val inboxStyle = NotificationCompat.InboxStyle()

            notificationBuilder.setStyle(inboxStyle)

            inboxStyle.setBigContentTitle(title)

            notificationDescObject.forEach { inboxStyle.addLine(it) }

            //Can set no.of messages as a summary.
            inboxStyle.setSummaryText(pushHandleStrategy.makeGroupSummary(notificationDescObject.size))
        }

        getNotificationManager(context).notify(groupId, notificationBuilder.build())
    }
}