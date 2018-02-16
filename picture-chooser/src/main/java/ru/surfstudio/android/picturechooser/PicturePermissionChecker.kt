package ru.surfstudio.android.picturechooser

import android.Manifest
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.base.permission.PermissionManager
import ru.surfstudio.android.core.ui.base.permission.PermissionRequest
import ru.surfstudio.android.dagger.scope.PerActivity
import javax.inject.Inject

/**
 * утилита для проверки и запроса пермишенов для камеры и хранилища
 */
@PerActivity
class PicturePermissionChecker @Inject constructor(private val permissionManager: PermissionManager) {

    /**
     * проверка и запрос пермишена на камеру и хранилища
     */
    fun checkCameraStoragePermission(): Observable<Boolean> {
        return permissionManager.checkObservable(CameraStoragePermissionRequest())
                .flatMap { isGranted ->
                    if (!isGranted)
                        permissionManager.request(CameraStoragePermissionRequest())
                    else Observable.just(true)
                }
    }

    /**
     * проверка и запрос пермишена на чтения из хранилища
     */
    fun checkGalleryStoragePermission(): Observable<Boolean> {
        return permissionManager.checkObservable(GalleryStoragePermissionRequest())
                .flatMap { isGranted ->
                    if (!isGranted)
                        permissionManager.request(GalleryStoragePermissionRequest())
                    else Observable.just(true)
                }
    }
}

/**
 * пермишен на запрос разрешения камеры
 */
class CameraStoragePermissionRequest : PermissionRequest() {

    override fun getPermissions() = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

/**
 * пермишен на запрос разрешения галлереи
 */
class GalleryStoragePermissionRequest : PermissionRequest() {
    override fun getPermissions() = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
}

