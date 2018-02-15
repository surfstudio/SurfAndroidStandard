package ru.surfstudio.android.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import ru.surfstudio.android.logger.Logger
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

/**
 * утилита получения пути к изображению по content uri
 */
object FileUtil {

    fun getRealPathFromImage(activity: Activity, uri: String) = getRealPathFromImage(activity, Uri.parse(uri))

    /**
     * получение реального пути к изображению используя Uri
     */
    fun getRealPathFromImage(activity: Activity, uri: Uri): String? {
        var realPath: String? = null
        if (isGooglePhotos(uri)) {
            try {
                val `is` = activity.contentResolver.openInputStream(uri)
                if (`is` != null) {
                    val pictureBitmap = BitmapFactory.decodeStream(`is`)
                    realPath = getRealPath(activity, getImageUri(activity, pictureBitmap))
                }
            } catch (e: FileNotFoundException) {
                Logger.e(e)
            }

        } else {
            realPath = getRealPath(activity, uri)
        }
        return realPath
    }

    private fun isGooglePhotos(uri: Uri): Boolean {
        return uri.toString().startsWith("content://com.google.android.apps.photos.content") && uri.toString().contains("mediakey")
    }

    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "img", null)
        return Uri.parse(path)
    }

    private fun getRealPath(activity: Activity, uri: Uri): String? {
        val cursor = activity.contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
        return null
    }
}