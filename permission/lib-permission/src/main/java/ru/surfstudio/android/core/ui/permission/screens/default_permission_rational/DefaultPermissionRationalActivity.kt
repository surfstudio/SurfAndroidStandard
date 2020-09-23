package ru.surfstudio.android.core.ui.permission.screens.default_permission_rational

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ru.surfstudio.android.core.ui.permission.R
import ru.surfstudio.android.core.ui.navigation.Route

/**
 * Экран объяснения причины запроса разрешения.
 */
class DefaultPermissionRationalActivity : AppCompatActivity() {

    private val permissionsRationalStr: String
        get() = intent.getStringExtra(Route.EXTRA_FIRST) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlertDialog
                .Builder(this)
                .setMessage(permissionsRationalStr)
                .setNeutralButton(R.string.default_permission_rational_got_it, null)
                .setOnDismissListener { finish() }
                .create()
                .show()
    }
}