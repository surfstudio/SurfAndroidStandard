package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.route.Route
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
class DefaultSettingsRationalRoute(
    val settingsRationalStr: String,
    val settingsPositiveButtonStr: String? = null,
    val settingsNegativeButtonStr: String? = null
) : ActivityWithResultRoute<Serializable>() {

    constructor(args: Bundle) : this(
        args[Route.EXTRA_FIRST] as String,
        args[Route.EXTRA_SECOND] as? String,
        args[Route.EXTRA_THIRD] as? String
    )

    override val uniqueId: String = settingsRationalStr

    override fun parseResultIntent(resultCode: Int, resultIntent: Intent?): Serializable {
        return resultCode == Activity.RESULT_OK
    }

    override fun getScreenClass() = DefaultSettingsRationalActivity::class.java

    override fun prepareData() = bundleOf(
        Route.EXTRA_FIRST to settingsRationalStr,
        Route.EXTRA_SECOND to settingsPositiveButtonStr,
        Route.EXTRA_THIRD to settingsNegativeButtonStr
    )
}
