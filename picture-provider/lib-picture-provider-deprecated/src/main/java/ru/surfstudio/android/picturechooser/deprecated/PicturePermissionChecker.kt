/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.picturechooser.deprecated

import android.Manifest
import android.os.Build
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.permission.deprecated.PermissionManager
import ru.surfstudio.android.core.ui.permission.deprecated.PermissionRequest

/**
 * утилита для проверки и запроса пермишенов для камеры и хранилища
 */
@Deprecated("Prefer using new implementation")
class PicturePermissionChecker @JvmOverloads constructor(
    private val permissionManager: PermissionManager,
    private val cameraPermissionRequest: PermissionRequest = CameraStoragePermissionRequest(),
    private val galleryStoragePermissionRequest: PermissionRequest = GalleryStoragePermissionRequest()
) {

    /**
     * проверка и запрос пермишена на камеру и хранилища
     */
    fun checkCameraStoragePermission(): Observable<Boolean> =
            permissionManager
                    .request(cameraPermissionRequest)
                    .toObservable()

    /**
     * проверка и запрос пермишена на чтения из хранилища
     */
    fun checkGalleryStoragePermission(): Observable<Boolean> =
            permissionManager
                    .request(galleryStoragePermissionRequest)
                    .toObservable()
}

/**
 * пермишен на запрос разрешения камеры
 */
@Deprecated("Prefer using new implementation")
open class CameraStoragePermissionRequest : PermissionRequest() {

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

/**
 * пермишен на запрос разрешения галереи
 */
@Deprecated("Prefer using new implementation")
open class GalleryStoragePermissionRequest : PermissionRequest() {

    override val permissions: Array<String>
        get() = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
}

