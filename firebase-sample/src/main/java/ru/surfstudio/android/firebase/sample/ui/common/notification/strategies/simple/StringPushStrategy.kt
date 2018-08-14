package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple

import android.app.Activity
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.StringNotificationTypeData

/**
 * Стратегия для пушей, возвращающих строковое значение
 */
class StringPushStrategy : BaseSimplePushStrategy<StringNotificationTypeData>() {
    override val typeData: StringNotificationTypeData
        get() = StringNotificationTypeData()

    override fun handlePushInActivity(activity: Activity): Boolean = false
}