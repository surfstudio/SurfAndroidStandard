package ru.surfstudio.android.push.sample.ui.common.notification.strategies.simple

import android.app.PendingIntent
import android.content.Context
import ru.surfstudio.android.push.sample.R
import ru.surfstudio.android.push.sample.ui.common.notification.strategies.type.NotificationTypeData
import ru.surfstudio.android.push.sample.ui.screen.push.data.DataPushActivityRoute
import androidx.core.app.NotificationCompat
import ru.surfstudio.android.notification.ui.notification.SHOULD_AUTO_CANCEL
import ru.surfstudio.android.notification.ui.notification.groups.NotificationsGroup

const val READ_ACTION_ID = "ru.surfstudio.android.firebase.sample.READ_ACTION"

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

    override fun makeNotificationBuilder(context: Context, title: String, body: String): NotificationCompat.Builder? {
        return super.makeNotificationBuilder(context, title, body)
                ?.addAction(-1, context.getString(R.string.notification_custom_action), createPendingIntent(context, title))
    }

    private fun createPendingIntent(context: Context, title: String): PendingIntent {
        val intent = getIntentForPending(context, group.id, pushId).apply {
            action = READ_ACTION_ID
            // we can turn off auto canceling to custom actions click
            putExtra(SHOULD_AUTO_CANCEL, false)
        }
        return PendingIntent.getBroadcast(context.applicationContext,
                title.hashCode(), intent, PendingIntent.FLAG_ONE_SHOT
        )
    }

    override fun coldStartIntent(context: Context) = DataPushActivityRoute(typeData.data).prepareIntent(context)
}