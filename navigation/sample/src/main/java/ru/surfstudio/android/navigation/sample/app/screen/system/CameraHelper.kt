package ru.surfstudio.android.navigation.sample.app.screen.system

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Класс-помощник для флоу получения фото с камеры.
 */
@PerScreen
class CameraHelper @Inject constructor(
        private val context: Context
) {

    /**
     * Добавление полученного фото в галерею устройства.
     */
    fun addMediaToGallery(uri: Uri?) {
        uri ?: return
        try {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = uri
            context.sendBroadcast(mediaScanIntent)
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    /**
     * Создание [File]'а, в который будет сохранено полученное с камеры фото.
     */
    fun generatePhotoPath(): File {
        val storageDir = getAlbumDir()
        val fileName = createPhotoName()
        return File(storageDir, fileName)
    }

    private fun getAlbumDir(): File {
        val sharedPicturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val appDirName = getAppDirName()
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            return File(sharedPicturesDir, appDirName)
                    .apply {
                        if (!mkdirs() && !exists()) {
                            throw Throwable("Failed to create directory")
                        }
                    }
        } else {
            throw Throwable("External storage is not mounted READ/WRITE.")
        }
    }

    private fun getAppDirName(): String {
        val packageManager = context.packageManager
        val appInfo = context.applicationInfo
        return packageManager.getApplicationLabel(appInfo).toString()
    }

    private fun createPhotoName(): String {
        val date = Date()
        date.time = System.currentTimeMillis() + Random().nextInt(1000) + 1
        val timeStamp = PHOTO_PATH_DATA_FORMATTER.format(date)
        return "IMG_$timeStamp.jpeg"
    }

    private companion object {
        val PHOTO_PATH_DATA_FORMATTER = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US)
    }
}
