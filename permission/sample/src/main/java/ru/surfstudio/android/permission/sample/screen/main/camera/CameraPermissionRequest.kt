package ru.surfstudio.android.permission.sample.screen.main.camera

import android.Manifest
import ru.surfstudio.android.core.ui.permission.PermissionRequest

class CameraPermissionRequest : PermissionRequest() {
    override val permissions: Array<String>
        get() = arrayOf(Manifest.permission.CAMERA)

    init {
        showPermissionsRational = true
    }
}
