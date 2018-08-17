package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple

import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.NotificationTypeData

/**
 * Стратегия для пушей с данными первого типа
 */
class DataPushStrategy : BaseSimplePushStrategy<NotificationTypeData>() {
    override val typeData: NotificationTypeData
        get() = NotificationTypeData()
}