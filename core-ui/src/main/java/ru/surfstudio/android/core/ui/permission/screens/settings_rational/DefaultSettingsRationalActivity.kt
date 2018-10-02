package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import ru.surfstudio.android.core.ui.R
import ru.surfstudio.android.core.ui.navigation.Route

private const val SETTINGS_REQUEST_CODE = 1005

/**
 * Экран объяснения необходимости перехода в настройки приложения.
 */
class DefaultSettingsRationalActivity : AppCompatActivity() {

    private val settingsRationalStr: String
        get() = intent.getStringExtra(Route.EXTRA_FIRST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlertDialog
                .Builder(this)
                .setMessage(settingsRationalStr)
                .setPositiveButton(R.string.settings_rational_go_to_settings) { _, _ ->
                    val settingsUri = Uri.fromParts("package", packageName, null)
                    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, settingsUri)
                    startActivityForResult(settingsIntent, SETTINGS_REQUEST_CODE)
                }
                .setNegativeButton(R.string.settings_rational_cancel, null)
                .setOnDismissListener { finish() }
                .create()
                .show()
    }
}