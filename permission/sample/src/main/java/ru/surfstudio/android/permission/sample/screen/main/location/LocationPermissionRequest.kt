package ru.surfstudio.android.permission.sample.screen.main.location

import android.Manifest
import ru.surfstudio.android.core.ui.permission.PermissionRequest

class LocationPermissionRequest : PermissionRequest() {

    override val permissions: Array<String>
        get() = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
}