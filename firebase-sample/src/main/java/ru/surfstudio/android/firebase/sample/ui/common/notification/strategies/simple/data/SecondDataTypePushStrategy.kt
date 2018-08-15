package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.data

import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.base.BaseSimplePushStrategy
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.NotificationTypeData

/**
 * Стратегия для пушей с данными второго типа
 */
class SecondDataTypePushStrategy : BaseSimplePushStrategy<NotificationTypeData>() {
    override val typeData: NotificationTypeData
        get() = NotificationTypeData()
}