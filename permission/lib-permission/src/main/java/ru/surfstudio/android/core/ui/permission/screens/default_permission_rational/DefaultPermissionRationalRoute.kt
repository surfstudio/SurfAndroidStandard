package ru.surfstudio.android.core.ui.permission.screens.default_permission_rational

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.route.Route

/**
 * Маршрут экрана объяснения причины запроса разрешения.
 */
class DefaultPermissionRationalRoute(
    val permissionRationalStr: String
) : ActivityWithResultRoute<Boolean>() {

    constructor(args: Bundle) : this(
        args[Route.EXTRA_FIRST] as String
    )

    override fun getScreenClass() = DefaultPermissionRationalActivity::class.java

    override fun prepareData() = bundleOf(
        Route.EXTRA_FIRST to permissionRationalStr
    )

    override val uniqueId: String = UNIQUE_ID

    override fun parseResultIntent(resultCode: Int, resultIntent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }

    private companion object {
        const val UNIQUE_ID = "DefaultPermissionRationalRoute"
    }
}
