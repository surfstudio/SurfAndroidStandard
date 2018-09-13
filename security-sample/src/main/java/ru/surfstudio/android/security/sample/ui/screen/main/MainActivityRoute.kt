package ru.surfstudio.android.security.sample.ui.screen.main

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут главного экрана
 * @param isReAuthState переавторизация после таймаута в бэкграунде
 */
class MainActivityRoute(private val isReAuthState: Boolean = false) : ActivityRoute() {

    override fun prepareIntent(context: Context) = Intent(context, MainActivityView::class.java)
            .apply {
                putExtra(Route.EXTRA_FIRST, isReAuthState)
                if (isReAuthState) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
}
