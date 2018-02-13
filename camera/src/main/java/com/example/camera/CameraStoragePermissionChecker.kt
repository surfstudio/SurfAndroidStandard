package com.example.camera

import android.Manifest
import io.reactivex.Observable
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.permission.PermissionManager
import ru.surfstudio.android.core.ui.base.permission.PermissionRequest
import javax.inject.Inject

/**
 * утилита для проверки и запроса пермишенов для камеры и хранилища
 */
@PerScreen
class CameraStoragePermissionChecker @Inject constructor(private val permissionManager: PermissionManager) {

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
     * проверка и запрос пермишена к хранилищу в случае его отсутствия
     */
    fun checkStoragePermission(): Observable<Boolean> {
        return permissionManager.checkObservable(StoragePermissionRequest())
                .flatMap { isGranted ->
                    if (!isGranted)
                        permissionManager.request(StoragePermissionRequest())
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
 * пермишен на запрос доступа к системному хранилищу
 */
class StoragePermissionRequest : PermissionRequest() {

    override fun getPermissions() = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}
