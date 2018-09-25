package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Маршрут экрана настроек приложения.
 */
class ApplicationSettingsRoute : ActivityRoute() {

    override fun prepareIntent(context: Context) =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
}