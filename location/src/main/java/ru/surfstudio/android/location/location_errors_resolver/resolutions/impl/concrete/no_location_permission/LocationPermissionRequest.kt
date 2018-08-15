package ru.surfstudio.android.location.location_errors_resolver.resolutions.impl.concrete.no_location_permission

import android.Manifest
import ru.surfstudio.android.core.ui.permission.PermissionRequest
import ru.surfstudio.android.location.domain.LocationAccuracy

/**
 * Запрос разрешения получения локации.
 *
 * @param locationAccuracy запрашиваемая точность определения метоположения.
 */
class LocationPermissionRequest(locationAccuracy: LocationAccuracy) : PermissionRequest() {

    private val requestingPermission = when (locationAccuracy) {
            LocationAccuracy.FINE -> Manifest.permission.ACCESS_FINE_LOCATION
            LocationAccuracy.COARSE -> Manifest.permission.ACCESS_COARSE_LOCATION
        }

    override fun getPermissions() = arrayOf(requestingPermission)
}