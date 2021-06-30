package ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.concrete.no_location_permission

import android.Manifest
import ru.surfstudio.android.core.ui.permission.deprecated.PermissionRequest

/**
 * Запрос разрешения на доступ к местоположению.
 */
@Deprecated("Prefer using new implementation")
open class LocationPermissionRequest : PermissionRequest() {

    final override val permissions: Array<String>
        get() = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
}