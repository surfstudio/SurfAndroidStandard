package ru.surfstudio.android.navigation.sample.app.screen.main.profile.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import android.provider.Settings

class ApplicationSettingsRoute : ActivityRoute() {

    override fun createIntent(context: Context): Intent =
            Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
            )
}
