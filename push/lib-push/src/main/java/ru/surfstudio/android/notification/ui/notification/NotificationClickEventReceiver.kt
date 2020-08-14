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
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.ui.PushClickProvider
import java.io.Serializable

const val NOTIFICATION_DATA = "notification_data"
internal const val NOTIFICATION_GROUP_ID = "notification_group_id"
internal const val EVENT_TYPE = "event_type"

internal enum class Event : Serializable {
    OPEN, DISMISS
}

/**
 * Ресивер для обработки событий пуш нотификаций
 * Обрабатывает тап на нотификацию и отклонение нотификации.
 */
class NotificationClickEventReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val event = intent.getSerializableExtra(EVENT_TYPE) as Event

        when(event) {
            Event.OPEN -> handleNotificationOpen(context, intent)
            Event.DISMISS -> handleNotificationDismiss(context, intent)
        }

        val groupId = intent.getIntExtra(NOTIFICATION_GROUP_ID, 0)
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
}