package ru.surfstudio.standard.f_debug.tools

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана показа Tools
 */
class ToolsDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, ToolsDebugActivityView::class.java)
    }
}