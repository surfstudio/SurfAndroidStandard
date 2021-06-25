package ru.surfstudio.android.core.ui.permission.deprecated.screens.settings_rational

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.core.ui.permission.deprecated.R
import ru.surfstudio.android.core.ui.navigation.Route

private const val SETTINGS_REQUEST_CODE = 1005

/**
 * Экран объяснения необходимости перехода в настройки приложения.
 */
@Deprecated(
    message = "Prefer using new implementation",
    replaceWith = ReplaceWith("permission.PermissionManager")
)
class DefaultSettingsRationalActivity : AppCompatActivity() {

    private val settingsRationalStr: String
        get() = intent.getStringExtra(Route.EXTRA_FIRST) ?: ""

    private val settingsPositiveButtonStr: String
        get() = intent.getStringExtra(Route.EXTRA_SECOND)
                ?: getString(R.string.settings_rational_go_to_settings)

    private val settingsNegativeButtonStr: String
        get() = intent.getStringExtra(Route.EXTRA_THIRD)
                ?: getString(R.string.settings_rational_cancel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlertDialog
                .Builder(this)
                .setMessage(settingsRationalStr)
                .setPositiveButton(settingsPositiveButtonStr) { _, _ ->
                    val settingsUri = Uri.fromParts("package", packageName, null)
                    val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, settingsUri)
                    startActivityForResult(settingsIntent, SETTINGS_REQUEST_CODE)
                }
                .setNegativeButton(settingsNegativeButtonStr, null)
                .setOnDismissListener { finish() }
                .create()
                .show()
    }
}