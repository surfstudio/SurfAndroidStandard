package ru.surfstudio.standard.base_ui.navigation

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.standard.base_ui.provider.route.RouteClassProvider

/**
 * Роут главного экрана
 */
class MainActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, RouteClassProvider.getActivityClass(this::class))
    }
}
