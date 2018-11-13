package ru.surfstudio.standard.f_debug.server_settings.reboot

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана перезапуска приложения
 */
class RebootDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, RebootActivityDebugView::class.java)
    }
}