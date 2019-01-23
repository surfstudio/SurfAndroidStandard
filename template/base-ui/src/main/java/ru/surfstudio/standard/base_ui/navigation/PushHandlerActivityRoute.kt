package ru.surfstudio.standard.base_ui.navigation

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithParamsRoute
import ru.surfstudio.standard.base_ui.provider.route.RouteClassProvider
import ru.surfstudio.standard.domain.notification.Notification

/**
 * Роут экрана обработки пуша
 *
 * @param notification пуш-уведомление, которое подлежит обработке
 **/
class PushHandlerActivityRoute(val notification: Notification) : ActivityWithParamsRoute() {

    constructor(intent: Intent) : this(
            intent.getSerializableExtra(Route.EXTRA_FIRST) as Notification
    )

    override fun prepareIntent(context: Context) =
            Intent(context, RouteClassProvider.getActivityClass(this::class))
                    .apply {
                        putExtra(Route.EXTRA_FIRST, notification)
                    }
}