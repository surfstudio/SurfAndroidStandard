package ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.simple

import android.app.PendingIntent
import android.content.Context
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.NotificationTypeData
import ru.surfstudio.android.firebase.sample.ui.screen.push.PushActivityRoute

/**
 * Стратегия для пушей с данными
 */
class DataPushStrategy : BaseSimplePushStrategy<NotificationTypeData>() {
    override val typeData: NotificationTypeData
        get() = NotificationTypeData()

    override fun coldStartRoute(): ActivityRoute = PushActivityRoute()

    override fun preparePendingIntent(context: Context, title: String): PendingIntent {
        return PendingIntent.getActivity(
                context,
                title.hashCode(),
                coldStartRoute().prepareIntent(context),
                PendingIntent.FLAG_ONE_SHOT)
    }
}