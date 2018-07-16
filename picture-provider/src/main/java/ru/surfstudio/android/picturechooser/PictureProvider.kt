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
package ru.surfstudio.android.picturechooser

import android.content.Context
import android.provider.MediaStore
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.picturechooser.exceptions.NoPermissionException


/**
 * Поставляет изображения находящиеся на устройстве или с камеры.
 */
class PictureProvider constructor(
        val context: Context,
        activityNavigator: ActivityNavigator,
        activityProvider: ActivityProvider,
        private val cameraStoragePermissionChecker: PicturePermissionChecker) {

    private val cameraIntentHelper = CameraPictureProvider(activityNavigator, activityProvider.get())
    private val galleryPictureProvider = GalleryPictureProvider(activityNavigator, activityProvider.get())

    /**
     *  Запускает сторонее приложение камеры для получения изображение.
     *  @return Observable Uri изображения и угол поворота.
     */
    fun openCameraAndTakePhoto(noPermissionAction: () -> Unit = {}): Observable<CameraPictureProvider.CameraResult> {
        return cameraStoragePermissionChecker.checkCameraStoragePermission()
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        cameraIntentHelper.startCameraIntent()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }

    /**
     *  Запускает сторонее приложение галереи для получения изображение.
     *  @return Observable путь до изображения (может вернуть пустое значение)
     */
    fun openGalleryAndGetPhoto(noPermissionAction: () -> Unit = {}): Observable<String> {
        return cameraStoragePermissionChecker.checkGalleryStoragePermission()
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        galleryPictureProvider.openGalleryForSingleImage()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }

    /**
     *  Запускает сторонее приложение галереи для получения изображение.
     *  @return Observable Uri.toString() изображения.
     */
    fun openGalleryAndGetPhotoUri(noPermissionAction: () -> Unit = {}): Observable<String> {
        return cameraStoragePermissionChecker.checkGalleryStoragePermission()
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        galleryPictureProvider.openGalleryForSingleImageUri()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }


    /**
     *  Запускает сторонее приложение галереи для получения изображение.
     *  @return Observable Uri изображения.
     */
    fun openGalleryAndGetFewPhoto(noPermissionAction: () -> Unit = {}): Observable<List<String>> {
        return cameraStoragePermissionChecker.checkGalleryStoragePermission()
                .flatMap { hasPermission ->
                    if (hasPermission) {
                        galleryPictureProvider.openGalleryForMultipleImage()
                    } else {
                        noPermissionAction()
                        Observable.error(NoPermissionException())
                    }
                }
    }

    /**
     * @return Uri всех изображений на устройстве
     */
    fun getAllStoragePhoto(): Observable<List<String>> {
        return Observable.create { subscriber ->
            val images = ArrayList<String>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT)

            context.contentResolver
                    .query(uri,
                            projection,
                            null,
                            null,
                            MediaStore.MediaColumns.DATE_ADDED + " DESC")
                    .apply {
                        val columnIndexData = getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                        val columnIndexWidth = getColumnIndex(MediaStore.MediaColumns.WIDTH)
                        val columnIndexHeight = getColumnIndex(MediaStore.MediaColumns.HEIGHT)
                        while (moveToNext()) {
                            val width = getInt(columnIndexWidth)
                            val height = getInt(columnIndexHeight)
                            //не добавляем некорректные изображения
                            if (width > 0 && height > 0) {
                                val absolutePathOfImage = getString(columnIndexData)
                                images.add(absolutePathOfImage)
                            }
                        }
                        close()
                    }
            subscriber.onNext(images)
            subscriber.onComplete()
        }
    }
}