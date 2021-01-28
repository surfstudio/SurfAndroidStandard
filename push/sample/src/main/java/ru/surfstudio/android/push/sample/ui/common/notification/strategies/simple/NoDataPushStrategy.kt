package ru.surfstudio.android.push.sample.ui.common.notification.strategies.simple

import android.content.Context
import ru.surfstudio.android.push.sample.R
import ru.surfstudio.android.push.sample.ui.common.notification.strategies.type.NoDataNotificationTypeData
import ru.surfstudio.android.push.sample.ui.screen.push.PushActivityRoute
import ru.surfstudio.android.notification.ui.notification.groups.NotificationsGroup

/**
 * Стратегия для пушей без данных
 */
class NoDataPushStrategy : BaseSimplePushStrategy<NoDataNotificationTypeData>() {
    override val typeData by lazy { NoDataNotificationTypeData() }

    override val channelName: Int = R.string.no_data_push_channel_name

    override val group: NotificationsGroup = NotificationsGroup("no_data_group")

    override fun makeGroupSummary(notificationCount: Int): String {
        return "No data notifications $notificationCount"
    }

    override fun coldStartIntent(context: Context) = PushActivityRoute().prepareIntent(context)
}