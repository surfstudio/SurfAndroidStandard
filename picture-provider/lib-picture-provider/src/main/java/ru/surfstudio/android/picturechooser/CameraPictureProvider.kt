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

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.picturechooser.exceptions.ActionInterruptedException
import ru.surfstudio.android.picturechooser.exceptions.ExternalStorageException
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


/**
 *  Позволяет получить данные с камеры стороннего приложения
 */
open class CameraPictureProvider(
        private val activityNavigator: ActivityNavigator,
        private val activityProvider: ActivityProvider,
        private val fileGenerator: PictureDestinationGenerator = CompatCameraPictureProviderDestinationGenerator(activityProvider)
) {

    private val currentActivity get() = activityProvider.get()

    @Deprecated(message = "", replaceWith = ReplaceWith(expression = ""))
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
     * Открывает экран камеры, результатом которой будет [UriWrapper]
     */
    fun startCameraWithUriResult(): Observable<UriWrapper> {
        val imageUri = fileGenerator.generatePictureUri()
        val route = CameraRoute(imageUri)
        val resultObservable = activityNavigator.observeResult<ResultData>(route).flatMap {
            if (it.isSuccess) {
                Observable.just(UriWrapper(imageUri))
            } else {
                Observable.error(ActionInterruptedException())
            }
        }
        activityNavigator.start(route)
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

interface PictureDestinationGenerator {
    fun generatePictureUri(): Uri
}

class CompatCameraPictureProviderDestinationGenerator(
        private val uriGenerator: PictureUriGenerator,
        private val fileGenerator: PictureFileGenerator
): PictureDestinationGenerator {

    constructor(activityProvider: ActivityProvider): this(
            uriGenerator = PictureUriGenerator(activityProvider),
            fileGenerator = PictureFileGenerator(activityProvider)
    )

    override fun generatePictureUri(): Uri {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            fileGenerator.generatePictureUri()
        } else {
            uriGenerator.generatePictureUri()
        }
    }
}

open class PictureUriGenerator(
        private val activityProvider: ActivityProvider,
        private val pictureNameGenerator: PictureNameGenerator = PictureNameGenerator()
): PictureDestinationGenerator {

    private val currentActivity get() = activityProvider.get()

//    @RequiresApi(Build.VERSION_CODES.Q)
    override fun generatePictureUri(): Uri {
        val values = createContentValues()
        return currentActivity.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        ) ?: error("Failed generate image uri for new photo")
    }

    @CallSuper
    protected open fun createContentValues(): ContentValues {
        return ContentValues().apply {
            val pictureName = pictureNameGenerator.generatePictureName()
            put(MediaStore.Images.Media.TITLE, pictureName)
        }
    }
}

open class PictureFileGenerator(
        private val activityProvider: ActivityProvider,
        private val pictureNameGenerator: PictureNameGenerator = PictureNameGenerator()
): PictureDestinationGenerator {

    private val currentActivity get() = activityProvider.get()

    override fun generatePictureUri(): Uri {
        val pictureFile = generatePictureFile()
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(
                    currentActivity,
                    currentActivity.applicationContext.packageName + ".provider",
                    pictureFile
            )
        } else {
            Uri.fromFile(pictureFile)
        }
    }

    /**
     * Создает [File], который будет использоваться для сохранения фото
     */
    fun generatePictureFile(): File {
        val pictureDir = getAlbumDir()
        val pictureName = generatePictureName()
        return File(pictureDir, pictureName)
    }

    /**
     * @return имя для фото
     */
    protected open fun generatePictureName(): String {
        return pictureNameGenerator.generatePictureName()
    }

    /**
     * @return [File], который будет указывать на директорию, где будет сохранено фото
     */
    private fun getAlbumDir(): File {
        val baseAlbumDir = getBaseAlbumDir()
        if (checkExternalBaseAlbumDir(baseAlbumDir)) {
            val albumName = getAlbumName()
            return File(baseAlbumDir, albumName).also {
                makeExternalBaseAlbumDirIfNotExists(it)
            }
        } else {
            throw ExternalStorageException("External storage is not mounted READ/WRITE.")
        }
    }

    /**
     * @return путь до внешнего хранилища в файловой системе андройда
     */
    protected open fun getBaseAlbumDir(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    }

    private fun checkExternalBaseAlbumDir(externalBaseAlbumDir: File): Boolean {
        return Environment.MEDIA_MOUNTED == if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        ) {
            Environment.getExternalStorageState(externalBaseAlbumDir)
        } else {
            Environment.getExternalStorageState()
        }
    }

    private fun makeExternalBaseAlbumDirIfNotExists(externalBaseAlbumDir: File) {
        if (!externalBaseAlbumDir.mkdirs()) {
            if (!externalBaseAlbumDir.exists()) {
                throw ExternalStorageException("Failed to create directory")
            }
        }
    }

    /**
     * @return имя для директории, в которой будет хранится фото относительно [getAlbumDir]
     */
    protected open fun getAlbumName(): String {
        val packageManager = currentActivity.packageManager
        val appInfo = currentActivity.applicationInfo
        return packageManager.getApplicationLabel(appInfo).toString()
    }
}

class PictureNameGenerator {
    /**
     * @return имя для фото
     */
    open fun generatePictureName(): String {
        val date = Date()
        date.time = System.currentTimeMillis() + Random().nextInt(1000) + 1
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date)
        return "IMG_$timeStamp.jpg"
    }
}