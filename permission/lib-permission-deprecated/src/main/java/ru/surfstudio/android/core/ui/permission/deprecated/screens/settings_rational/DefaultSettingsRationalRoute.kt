package ru.surfstudio.android.core.ui.permission.deprecated.screens.settings_rational

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import java.io.Serializable

/**
 * Маршрут экрана объяснения необходимости перехода в настройки приложения.
 *
 * Route of screen for go to settings to give permissions
 *
 * @param settingsRationalStr rationale message
 * @param settingsPositiveButtonStr positive button text
 * @param settingsNegativeButtonStr negative button text
 */
@Deprecated("Prefer using new implementation")
class DefaultSettingsRationalRoute(
        private val settingsRationalStr: String,
        private val settingsPositiveButtonStr: String? = null,
        private val settingsNegativeButtonStr: String? = null
) : ActivityWithResultRoute<Serializable>() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, DefaultSettingsRationalActivity::class.java)
                    .apply {
                        putExtra(Route.EXTRA_FIRST, settingsRationalStr)
                        putExtra(Route.EXTRA_SECOND, settingsPositiveButtonStr)
                        putExtra(Route.EXTRA_THIRD, settingsNegativeButtonStr)
                    }
}