package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import java.io.Serializable

/**
 * Маршрут экрана объяснения необходимости перехода в настройки приложения.
 */
class DefaultSettingsRationalRoute(private val settingsRationalStr: String) : ActivityWithResultRoute<Serializable>() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, DefaultSettingsRationalActivity::class.java)
                    .apply { putExtra(Route.EXTRA_FIRST, settingsRationalStr) }
}