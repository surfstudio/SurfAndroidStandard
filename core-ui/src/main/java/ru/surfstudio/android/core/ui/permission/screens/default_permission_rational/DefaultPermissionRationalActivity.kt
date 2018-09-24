package ru.surfstudio.android.core.ui.permission.screens.default_permission_rational

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import ru.surfstudio.android.core.ui.R
import ru.surfstudio.android.core.ui.navigation.Route

/**
 * Экран объяснения причины запроса разрешения.
 */
class DefaultPermissionRationalActivity : AppCompatActivity() {

    private val permissionsRationalStr: String
        get() = intent.getStringExtra(Route.EXTRA_FIRST)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AlertDialog
                .Builder(this)
                .setMessage(permissionsRationalStr)
                .setNeutralButton(R.string.default_permission_rational_got_it) { _, _ -> finish() }
                .create()
                .show()
    }
}