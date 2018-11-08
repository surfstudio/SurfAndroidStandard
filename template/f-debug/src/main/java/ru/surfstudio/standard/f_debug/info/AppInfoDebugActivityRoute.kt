package ru.surfstudio.standard.f_debug.info

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана показа общей информации
 */
class AppInfoDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, AppInfoDebugActivityView::class.java)
    }
}