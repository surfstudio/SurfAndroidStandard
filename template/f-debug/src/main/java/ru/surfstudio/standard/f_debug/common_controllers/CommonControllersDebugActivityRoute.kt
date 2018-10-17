package ru.surfstudio.standard.f_debug.common_controllers

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана для показа переиспользуемых контроллеров
 */
class CommonControllersDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, CommonControllersDebugActivityView::class.java)
    }
}
