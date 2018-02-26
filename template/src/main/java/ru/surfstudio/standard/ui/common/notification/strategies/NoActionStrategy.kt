package ru.trinitydigital.poison.ui.common.notification.strategies

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import ru.surf.android.notification.interactor.push.NoActionNotificationTypeData
import ru.surf.android.notification.ui.notification.SimpleAbstractPushHandleStrategy
import ru.surfstudio.android.core.ui.base.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.notification.interactor.push.NoActionNotificationTypeData
import ru.surfstudio.android.notification.ui.notification.strategies.SimpleAbstractPushHandleStrategy
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.common.notification.strategies.BasePushStrategy
import ru.surfstudio.standard.ui.screen.main.MainActivityRoute
import ru.trinitydigital.poison.R
import ru.trinitydigital.poison.ui.screen.main.MainActivityRoute

/**
 * Базовая стратегия уведомлений
 */
class NoActionStrategy : BasePushStrategy<NoActionNotificationTypeData>() {
    override val typeData: NoActionNotificationTypeData
        get() = NoActionNotificationTypeData()

    override val color: Int = R.color.colorPrimary

    override fun coldStartRoute(): ActivityRoute = MainActivityRoute()

    override fun handlePushInActivity(activity: Activity): Boolean = false


    override fun preparePendingIntent(context: Context, title: String): PendingIntent =
            PendingIntent.getActivity(
                    context,
                    title.hashCode(),
                    MainActivityRoute().prepareIntent(context),
                    PendingIntent.FLAG_ONE_SHOT)
}