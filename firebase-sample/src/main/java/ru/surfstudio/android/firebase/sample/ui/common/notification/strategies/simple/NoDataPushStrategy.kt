package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple

import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple.base.BaseSimplePushStrategy
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.NoDataNotificationTypeData

/**
 * Стратегия для пушей без данных
 */
class NoDataPushStrategy : BaseSimplePushStrategy<NoDataNotificationTypeData>() {
    override val typeData: NoDataNotificationTypeData
        get() = NoDataNotificationTypeData()
}