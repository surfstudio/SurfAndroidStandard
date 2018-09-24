package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import ru.surfstudio.android.core.ui.R
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator

/**
 * Экран объяснения необходимости перехода в настройки приложения.
 */
class SettingsRationalActivity : AppCompatActivity() {

    lateinit var activityNavigator: ActivityNavigator

    private val settingsRationalStr: String
        get() = intent.getStringExtra(Route.EXTRA_FIRST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlertDialog
                .Builder(this)
                .setMessage(settingsRationalStr)
                .setPositiveButton(R.string.settings_rational_go_to_settings) { _, _ ->
                    activityNavigator.start(ApplicationSettingsRoute())
                }
                .setNegativeButton(R.string.settings_rational_cancel, null)
                .create()
                .show()
    }
}