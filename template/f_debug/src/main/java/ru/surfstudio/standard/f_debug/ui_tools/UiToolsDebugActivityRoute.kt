package ru.surfstudio.standard.f_debug.ui_tools

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана показа UI-tools
 */
class UiToolsDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, UiToolsDebugActivityView::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}