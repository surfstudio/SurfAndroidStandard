package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import java.io.Serializable

/**
 * Маршрут экрана объяснения необходимости перехода в настройки приложения.
 *
 * Route of screen for going to settings to give permissions
 *
 * @param params dialog for going to settings params like:
 * rationale message, positive button text, negative button text
 */
class DefaultSettingsRationalRoute(
        private val params: SettingsRationalDialogParams
) : ActivityWithResultRoute<Serializable>() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, DefaultSettingsRationalActivity::class.java)
                    .apply {
                        putExtra(Route.EXTRA_FIRST, params)
                    }
}