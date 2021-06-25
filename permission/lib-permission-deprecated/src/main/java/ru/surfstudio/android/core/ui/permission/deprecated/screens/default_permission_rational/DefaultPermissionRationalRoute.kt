package ru.surfstudio.android.core.ui.permission.deprecated.screens.default_permission_rational

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import java.io.Serializable

/**
 * Маршрут экрана объяснения причины запроса разрешения.
 */
@Deprecated(
    message = "Prefer using new implementation",
    replaceWith = ReplaceWith("permission.PermissionManager")
)
class DefaultPermissionRationalRoute(
        private val permissionRationalStr: String
) : ActivityWithResultRoute<Serializable>() {

    override fun prepareIntent(context: Context?): Intent =
            Intent(context, DefaultPermissionRationalActivity::class.java)
                    .apply { putExtra(Route.EXTRA_FIRST, permissionRationalStr) }
}