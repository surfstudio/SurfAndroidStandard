package ru.surfstudio.standard.application.notification.strategy

import android.app.Activity
import android.content.Context
import android.content.Intent
import ru.surfstudio.android.notification.ui.notification.groups.NotificationsGroup
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.application.notification.type.NotificationTypeData
import ru.surfstudio.standard.ui.navigation.MainActivityRoute

/**
 * Базовая стратегия обработки push-уведомления
 * todo исправит исходя из нужд приложения
 */
class BasePushHandleStrategy : SimpleAbstractPushHandleStrategy<NotificationTypeData>() {

    override val typeData by lazy { NotificationTypeData() }

    override val channelId: Int
        get() = R.string.notification_channel_id

    override val channelName: Int
        get() = R.string.notification_channel_name

    override val icon: Int
        get() = R.mipmap.ic_launcher

    override val color: Int
        get() = R.color.colorAccent

    //todo исправить из нужд приложения
    override val group = NotificationsGroup("Messages")

    //todo исправить из нужд приложения
    override fun coldStartIntent(context: Context): Intent? =
            MainActivityRoute().prepareIntent(context)

    override fun handlePushInActivity(activity: Activity) = false
}