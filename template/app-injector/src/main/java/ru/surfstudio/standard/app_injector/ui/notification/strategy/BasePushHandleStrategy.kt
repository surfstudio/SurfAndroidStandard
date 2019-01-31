package ru.surfstudio.standard.app_injector.ui.notification.strategy

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.surfstudio.android.notification.ui.notification.groups.NotificationsGroup
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy
import ru.surfstudio.android.template.app_injector.R
import ru.surfstudio.standard.app_injector.ui.notification.type.NotificationTypeData
import ru.surfstudio.standard.base_ui.navigation.PushHandlerActivityRoute
import ru.surfstudio.standard.domain.notification.Notification

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
        get() = R.drawable.ic_launcher_background

    override val color: Int
        get() = R.color.colorAccent

    override val group = NotificationsGroup("Messages")

    override fun preparePendingIntent(context: Context, title: String): PendingIntent {
        return PendingIntent.getActivity(
                context,
                title.hashCode(),
                coldStartIntent(context),
                PendingIntent.FLAG_ONE_SHOT)
    }

    override fun coldStartIntent(context: Context): Intent =
            PushHandlerActivityRoute(typeData.data ?: Notification() ).prepareIntent(context)

    override fun handlePushInActivity(activity: Activity) = false
}