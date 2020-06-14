package ru.surfstudio.android.push.sample.ui.common.notification.strategies.simple

import android.content.Context
import ru.surfstudio.android.push.sample.R
import ru.surfstudio.android.push.sample.ui.common.notification.strategies.type.NotificationTypeData
import ru.surfstudio.android.push.sample.ui.screen.push.data.DataPushActivityRoute
import ru.surfstudio.android.notification.ui.notification.groups.NotificationsGroup

/**
 * Стратегия для пушей с данными
 */
class DataPushStrategy : BaseSimplePushStrategy<NotificationTypeData>() {

    override val typeData by lazy { NotificationTypeData() }

    override val channelName: Int = R.string.data_push_channel_name

    override val group: NotificationsGroup = NotificationsGroup("notification_data")

    override fun makeGroupSummary(notificationCount: Int): String {
        return "Data notifications $notificationCount"
    }

    override fun coldStartIntent(context: Context) = DataPushActivityRoute(typeData.data).prepareIntent(context)
}