package ru.surfstudio.standard.f_debug.server_settings

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана настроек сервера
 */
class ServerSettingsDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context?): Intent {
        return Intent(context, ServerSettingsDebugActivityView::class.java)
    }
}