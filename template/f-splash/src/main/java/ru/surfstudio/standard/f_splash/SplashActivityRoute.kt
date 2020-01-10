package ru.surfstudio.standard.f_splash

import android.content.Context
import android.content.Intent

import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy

/**
 * Маршрут сплеша
 */
class SplashRoute(val pushHandleStrategy: PushHandleStrategy<*>? = null) : ActivityRoute() {

    override fun prepareIntent(context: Context) = Intent(context, SplashActivityView::class.java)
}