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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.ui.PushClickProvider

const val NOTIFICATION_DATA = "notification_data"
const val NOTIFICATION_GROUP_ID = "notification_group_id"
const val EVENT_TYPE = "event_type"
const val NOTIFICATION_ID = "notification_id"
const val SHOULD_AUTO_CANCEL = "should_auto_cancel_on_action"

internal enum class Event {
    OPEN, DISMISS
}

/**
 * Ресивер для обработки событий пуш нотификаций
 * Обрабатывает тап на нотификацию и отклонение нотификации.
 */
class NotificationClickEventReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.getSerializableExtra(EVENT_TYPE) as? Event) {
            Event.OPEN -> handleNotificationOpen(context, intent)
            Event.DISMISS -> handleNotificationDismiss(context, intent)
            else -> {
                handleNotificationActionClick(context, intent)
                return
            }
        }

        val groupId = getGroupId(intent)
        NotificationGroupHelper.clearSavedNotificationsForGroup(context, groupId)
    }

    private fun handleNotificationOpen(context: Context, intent: Intent) {
        Logger.i("Notification open: $intent")
        PushClickProvider.pushEventListener?.pushOpenListener(context, intent)
    }

    private fun handleNotificationDismiss(context: Context, intent: Intent) {
        Logger.i("Notification dismissed: $intent")
        PushClickProvider.pushEventListener?.pushDismissListener(context, intent)
    }

    private fun handleNotificationActionClick(context: Context, intent: Intent) {
        Logger.i("Notification action clicked: ${intent.action}")
        if (intent.getBooleanExtra(SHOULD_AUTO_CANCEL, true)) {
            val groupId = getGroupId(intent)
            val notificationId = intent.getIntExtra(NOTIFICATION_ID, -1)
            NotificationManagerHelper.cancel(context, groupId, notificationId)
        }
        PushClickProvider.pushEventListener?.customActionListener(context, intent)
    }

    private fun getGroupId(intent: Intent): Int {
        return intent.getIntExtra(NOTIFICATION_GROUP_ID, 0)
    }
}