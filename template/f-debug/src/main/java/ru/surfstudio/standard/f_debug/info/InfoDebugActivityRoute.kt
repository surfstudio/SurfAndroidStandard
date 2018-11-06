package ru.surfstudio.standard.f_debug.info

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана показа общей информации
 */
class InfoDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, InfoDebugActivityView::class.java)
    }
}