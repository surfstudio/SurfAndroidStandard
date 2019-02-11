package ru.surfstudio.android.notification.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.PushClickProvider

const val NOTIFICATION_DATA = "notification_data"
internal const val NOTIFICATION_GROUP_ID = "notification_group_id"
internal const val EVENT_TYPE = "event_type"

internal enum class Event {
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