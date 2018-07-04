package ru.surfstudio.standard.ui.common.notification.strategies

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.notification.interactor.push.NoActionNotificationTypeData
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.screen.main.MainActivityRoute

/**
 * Стратегия уведомлений без действия
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