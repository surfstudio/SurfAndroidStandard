package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type

import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData

/**
 * Тип пуша с данными типа String
 */
class StringNotificationTypeData : BaseNotificationTypeData<String>() {
    override fun extractData(map: Map<String, String>): String? {
        TODO("not implemented")
    }
}