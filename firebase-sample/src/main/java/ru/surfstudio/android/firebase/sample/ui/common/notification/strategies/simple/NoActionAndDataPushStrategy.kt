package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple

import android.app.Activity
import ru.surfstudio.android.notification.interactor.push.NoActionNotificationTypeData

/**
 * Стратегия для пушей без данных и без действия
 */
class NoActionAndDataPushStrategy : BaseSimplePushStrategy<NoActionNotificationTypeData>() {
    override val typeData: NoActionNotificationTypeData
        get() = NoActionNotificationTypeData()

    override fun handlePushInActivity(activity: Activity): Boolean = false
}