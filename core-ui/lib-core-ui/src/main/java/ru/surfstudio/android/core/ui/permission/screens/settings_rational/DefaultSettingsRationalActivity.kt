package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.core.ui.R
import ru.surfstudio.android.core.ui.navigation.Route

private const val SETTINGS_REQUEST_CODE = 1005

/**
 * Экран объяснения необходимости перехода в настройки приложения.
 */
class DefaultSettingsRationalActivity : AppCompatActivity() {

    private val params: SettingsRationalDialogParams
        get() = intent.getSerializableExtra(Route.EXTRA_FIRST) as SettingsRationalDialogParams

    private val settingsRationalStr: String
        get() = params.rationalTxt ?: getString(R.string.settings_rational_msg)

    private val settingsPositiveButtonStr: String
        get() = params.positiveButtonTxt ?: getString(R.string.settings_rational_go_to_settings)

    private val settingsNegativeButtonStr: String
        get() = params.negativeButtonTxt ?: getString(R.string.settings_rational_cancel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlertDialog
                .Builder(this)
                .setMessage(settingsRationalStr)
                .setPositiveButton(settingsPositiveButtonStr) { _, _ ->
                    val settingsUri = Uri.fromParts("package", packageName, null)
                    val settingsIntent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, settingsUri)
                    startActivityForResult(settingsIntent, SETTINGS_REQUEST_CODE)
                }
                .setNegativeButton(settingsNegativeButtonStr, null)
                .setOnDismissListener { finish() }
                .create()
                .show()
    }
}