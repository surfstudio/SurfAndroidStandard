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
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.picturechooser.destination.*
import ru.surfstudio.android.picturechooser.exceptions.ActionInterruptedException
import ru.surfstudio.android.picturechooser.exceptions.ExternalStorageException
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


/**
 *  Позволяет получить данные с камеры стороннего приложения
 */
class CameraPictureProvider(
        private val activityNavigator: ActivityNavigator,
        private val activityProvider: ActivityProvider,
        private val destinationProvider: PictureDestinationProvider = ContentResolverUriProvider(
                contentResolver = activityProvider.get().applicationContext.contentResolver,
                pictureTableProvider = object : PictureTableProvider {
                    override fun providePictureTable(): Uri {
                        return MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    }
                }
        )
) {

    private val currentActivity get() = activityProvider.get()

    @Deprecated(
            message = "This method is deprecated, because startCameraIntent use " +
                    "Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) " +
                    "for root directory for new picture, but after 29 api " +
                    "getExternalStoragePublicDirectory not working",
            replaceWith = ReplaceWith(expression = "startCameraWithUriResult"),
            level = DeprecationLevel.WARNING
    )
    fun startCameraIntent(): Observable<CameraResult> {
        val image = generatePicturePath()
        val route = CameraRoute(image)

        val result = activityNavigator.observeResult<ResultData>(route)
                .flatMap { result ->
                    if (result.isSuccess) {
                        Observable.just(
                                CameraResult(
                                        image.absolutePath,
                                        extractRotation(image.absolutePath)
                                )
                        )
                    } else {
                        Observable.error(ActionInterruptedException())
                    }
                }
                .doOnNext { _ ->
                    addMediaToGallery(image.absolutePath)
                }

        activityNavigator.startForResult(route)
        return result
    }

    /**
     * Открывает экран камеры, результатом которой будет [UriWrapper], содержащий [Uri], указывающий
     * на запись в таблице с информацией о новом изображении
     */
    fun startCameraWithUriResult(): Observable<UriWrapper> {
        val imageUri = destinationProvider.provideDestination()
        val route = CameraRoute(imageUri)
        val resultObservable = activityNavigator.observeResult<ResultData>(route).flatMap {
            if (it.isSuccess) {
                Observable.just(UriWrapper(imageUri))
            } else {
                Observable.error(ActionInterruptedException())
            }
        }.doOnError {
            destinationProvider.deleteDestination(imageUri)
        }
        activityNavigator.startForResult(route)
        return resultObservable
    }

    private fun extractRotation(photoPath: String): Int {
        try {
            val ei = ExifInterface(photoPath)
            val exif = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            return when (exif) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
        return 0
    }

    private fun addMediaToGallery(fromPath: String?) {
        if (fromPath == null) {
            return
        }
        val f = File(fromPath)
        val contentUri = Uri.fromFile(f)
        addMediaToGallery(contentUri)
    }

    private fun addMediaToGallery(uri: Uri?) {
        uri ?: return
        try {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = uri
            currentActivity.sendBroadcast(mediaScanIntent)
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    /**
     * Создает [File], который будет использоваться для сохранения фото
     */
    private fun generatePicturePath(): File {
        val storageDir = getAlbumDir()
        val pictureName = generatePictureName()
        return File(storageDir, pictureName)
    }

    /**
     * Создает имя для фото
     */
    private fun generatePictureName(): String {
        val date = Date()
        date.time = System.currentTimeMillis() + Random().nextInt(1000) + 1
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date)
        return "IMG_$timeStamp.jpg"
    }

    /**
     * Создаст [File], который будет указывать на директорию, где будет сохранено фото
     */
    private fun getAlbumDir(): File {
        val sharedPicturesDir = getBaseAlbumDir()
        val appDirName = getAppDirName()
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            return File(sharedPicturesDir, appDirName)
                    .apply {
                        if (!mkdirs()) {
                            if (!exists()) {
                                throw ExternalStorageException("Failed to create directory")
                            }
                        }
                    }
        } else {
            throw ExternalStorageException("External storage is not mounted READ/WRITE.")
        }
    }

    private fun getBaseAlbumDir(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }

    private fun getAppDirName(): String {
        val packageManager = currentActivity.packageManager
        val appInfo = currentActivity.applicationInfo
        return packageManager.getApplicationLabel(appInfo).toString()
    }

    private inner class CameraRoute(val imageUri: Uri) : ActivityWithResultRoute<ResultData>() {

        constructor(imageFile: File): this(
                imageFile.let {
                    if (Build.VERSION.SDK_INT >= 24) {
                        FileProvider.getUriForFile(
                                currentActivity,
                                currentActivity.applicationContext.packageName + ".provider",
                                imageFile
                        )
                    } else {
                        Uri.fromFile(imageFile)
                    }
                }
        )

        override fun prepareIntent(context: Context?): Intent {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                if (Build.VERSION.SDK_INT >= 24) {
                    addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }
            return Intent.createChooser(
                    takePictureIntent,
                    currentActivity.getString(R.string.choose_app)
            )
        }
    }

    private data class ResultData(val photoPath: String) : Serializable
}

data class CameraResult(val photoUrl: String, val rotation: Int)