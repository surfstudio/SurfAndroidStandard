package ru.surfstudio.standard.f_debug.system_settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS

private const val SCHEME_PACKAGE = "package"

class AppSettingsDebugActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
            .apply { data = Uri.fromParts(SCHEME_PACKAGE, context.packageName, null) }
    }
}