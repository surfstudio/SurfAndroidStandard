package ru.surfstudio.standard.f_debug.reused_components

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана для показа переиспользуемых компонентов
 */
class ReusedComponentsDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, ReusedComponentsDebugActivityView::class.java)
    }
}
