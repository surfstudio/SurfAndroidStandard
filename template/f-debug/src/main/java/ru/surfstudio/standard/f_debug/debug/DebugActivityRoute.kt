package ru.surfstudio.standard.f_debug.debug

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана показа информации для дебага
 */
class DebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, DebugActivityView::class.java)
    }
}
