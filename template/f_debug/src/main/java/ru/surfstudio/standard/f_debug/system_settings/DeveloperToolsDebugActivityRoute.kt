package ru.surfstudio.standard.f_debug.system_settings

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class DeveloperToolsDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
    }
}