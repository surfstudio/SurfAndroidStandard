package ru.surfstudio.android.location

import android.Manifest
import ru.surfstudio.android.core.ui.base.permission.PermissionRequest

/**
 * Объект запроса пермишнов на доступ к геолокационному сервису
 */
class LocationPermissionRequest : PermissionRequest() {
    override fun getPermissions(): Array<String> =
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
}
