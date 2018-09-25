package ru.surfstudio.standard.f_debug.debug_controllers

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана для показа контроллеров, используемых в приложении
 */
class DebugControllersActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, DebugControllersActivityView::class.java)
    }
}
