package ru.surfstudio.android.permission.sample.screen.main.camera

import android.Manifest
import android.os.Build
import ru.surfstudio.android.core.ui.permission.PermissionRequest

class CameraPermissionRequest : PermissionRequest() {

    /**
     * On Android 10 (API level 29) and higher, the proper directory for sharing photos is
     * the MediaStore.Images table. You don't need to declare any storage permissions,
     * as long as your app only needs to access the photos that the user took using your app.
     */
    override val permissions: Array<String> = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA
        )
    }
}
