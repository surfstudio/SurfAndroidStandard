package ru.surfstudio.android.push.sample.ui.common.notification.strategies.type

import ru.surfstudio.android.push.sample.domain.notification.Notification
import ru.surfstudio.android.push.sample.domain.notification.NotificationType
import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Тип пуша с данными
 */
class NotificationTypeData : BaseNotificationTypeData<Notification>() {
    companion object {
        private const val MINIMAL_NOTIFICATIONS_COUNT = 1
    }

    override fun extractData(map: Map<String, String>): Notification? = Notification(
            map["id"]?.toLong() ?: 0,
            NotificationType.getById(map["event"]?.toInt()),
            map["isGrouped"]?.toBoolean() ?: false,
            map["notificationsCount"]?.toInt() ?: MINIMAL_NOTIFICATIONS_COUNT,
            map["date"] ?: EMPTY_STRING,
            map["previewText"] ?: EMPTY_STRING
    )
}