package ru.surfstudio.android.navigation.observer.route

import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Route for requesting permission
 * This is used with [RequestPermission] command.
 */
class PermissionRequestRoute(
    val permissions: Array<String>
) : ActivityRoute(), ResultRoute<Boolean> {

    override fun getId(): String {
        return permissions.joinToString()
    }
}