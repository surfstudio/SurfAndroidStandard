package ru.surfstudio.standard.f_splash

import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.notification.ui.notification.strategies.PushHandleStrategy

/**
 * Маршрут сплеша
 */
class SplashRoute(val pushHandleStrategy: PushHandleStrategy<*>? = null) : ActivityRoute() {

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.standard.f_splash.SplashActivityView"
    }
}