package ru.surfstudio.standard.ui.common.notification.strategies

import ru.surfstudio.android.notification.interactor.push.BaseNotificationTypeData
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy
import ru.surfstudio.standard.R

/**
 * Базовый пуш
 */
abstract class BasePushStrategy<out T : BaseNotificationTypeData<*>> : SimpleAbstractPushHandleStrategy<T>() {
    override val color: Int = R.color.colorPrimary

    override val channelId: Int
        get() = R.string.channel_id
    override val icon: Int
        get() = R.drawable.navigation_empty_icon //todo иконка пуш уведомления
    override val autoCancelable: Boolean
        get() = true
}