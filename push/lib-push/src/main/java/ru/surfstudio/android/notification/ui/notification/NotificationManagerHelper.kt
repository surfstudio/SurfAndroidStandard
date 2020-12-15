/*
  Copyright (c) 2018-present, SurfStudio LLC, Akhbor Akhrorov.

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

import android.app.NotificationManager
import android.content.Context
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Отменяет уведомление заданной notificationId и при необходимости очищает суммарное уведомление.
 *
 * Если нужно программно отменить уведомление, то нужно использовать данный класс а не вывать
 * [NotificationManager.cancel] напрямую.
 */
object NotificationManagerHelper {

    /**
     * Отменяет уведомление по заданной notificationId
     *
     * @param groupId идентификатор группы
     * @param notificationId идентификатор уведомления
     */
    fun cancel(context: Context, groupId: Int, notificationId: Int) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var cancelSummary = false

        if (SdkUtils.isAtLeastNougat() && groupId != 0) {
            val statusBarNotifications = notificationManager.activeNotifications
            val groupKey = statusBarNotifications.firstOrNull { it.id == notificationId }?.groupKey
            val notificationsCountInGroup = statusBarNotifications.count { it.groupKey == groupKey }
            cancelSummary = notificationsCountInGroup == NOTIFICATION_WITH_SUMMARY_COUNT
        }
        notificationManager.cancel(notificationId)
        if (cancelSummary) {
            notificationManager.cancel(groupId)
        }

        NotificationGroupHelper.clearSavedNotificationsForGroup(context, groupId)
    }

    private const val NOTIFICATION_WITH_SUMMARY_COUNT = 2
}