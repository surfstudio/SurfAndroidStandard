package ru.surfstudio.standard.app_injector.ui.notification.strategies

import android.app.PendingIntent
import android.content.Context
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.standard.app_injector.ui.notification.strategies.base.BasePushHandleStrategy
import ru.surfstudio.standard.app_injector.ui.notification.types.DebugNotificationTypeData
import ru.surfstudio.standard.f_debug.DebugActivityRoute

/**
 * Стратегия обработки пуша для перехода на экран с информацией для дебага
 */
class DebugPushHandleStrategy : BasePushHandleStrategy<DebugNotificationTypeData>() {

    override val typeData by lazy { DebugNotificationTypeData() }

    override fun coldStartRoute(): ActivityRoute = DebugActivityRoute()

    override fun preparePendingIntent(context: Context, title: String): PendingIntent {
        return PendingIntent.getActivity(
                context,
                title.hashCode(),
                coldStartRoute().prepareIntent(context),
                PendingIntent.FLAG_ONE_SHOT)
    }
}