package ru.surfstudio.android.core.ui.permission.screens.settings_rational

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.core.ui.permission.R
import ru.surfstudio.android.navigation.route.activity.getDataBundle

/**
 * Экран объяснения необходимости перехода в настройки приложения.
 */
class DefaultSettingsRationalActivity : AppCompatActivity() {

    private lateinit var route: DefaultSettingsRationalRoute

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            setResult(it.resultCode, it.data)
            finish()
        }

    private val settingsRationalStr: String
        get() = route.settingsRationalStr

    private val settingsPositiveButtonStr: String
        get() = route.settingsPositiveButtonStr
            ?: getString(R.string.settings_rational_go_to_settings)

    private val settingsNegativeButtonStr: String
        get() = route.settingsNegativeButtonStr
            ?: getString(R.string.settings_rational_cancel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        route = DefaultSettingsRationalRoute(intent.getDataBundle() ?: Bundle.EMPTY)

        AlertDialog
            .Builder(this)
            .setMessage(settingsRationalStr)
            .setPositiveButton(settingsPositiveButtonStr) { _, _ ->
                val settingsUri = Uri.fromParts("package", packageName, null)
                val settingsIntent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, settingsUri)
                launcher.launch(settingsIntent)
            }
            .setNegativeButton(settingsNegativeButtonStr, null)
            .setOnDismissListener { finish() }
            .create()
            .show()
    }
}