package ru.surfstudio.standard.f_debug.server_settings.reboot.route

import android.content.Context
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class LauncherActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context) =
            context.packageManager.getLaunchIntentForPackage(context.packageName)
}