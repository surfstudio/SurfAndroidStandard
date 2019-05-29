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
package ru.surfstudio.android.utilktx.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import ru.surfstudio.android.logger.Logger
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

/**
 * Файловая утилита
 */
object FileUtil {

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    fun getRealPath(context: Context, uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosDocument(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
        } else if (ContentResolver.SCHEME_FILE.equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String? = null, selectionArgs: Array<String>? = null): String? {
        var result: String? = null
        uri?.let {
            val cursor = context.contentResolver.query(it, arrayOf(MediaStore.Images.ImageColumns.DATA),
                    selection, selectionArgs, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
        }
        return result
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri) = "com.android.externalstorage.documents" == uri.authority

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri) = "com.android.providers.downloads.documents" == uri.authority

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri) = "com.android.providers.media.documents" == uri.authority

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosDocument(uri: Uri) = "com.google.android.apps.photos.content" == uri.authority

    @Deprecated("use getRealPath")
    fun getRealPathFromImage(context: Context, uri: String) = getRealPathFromImage(context, Uri.parse(uri))

    /**
     * получение реального пути к изображению используя Uri
     */
    @Deprecated("use getRealPath")
    fun getRealPathFromImage(context: Context, uri: Uri): String? {
        var realPath: String? = null
        if (isGooglePhotos(uri)) {
            try {
                val `is` = context.contentResolver.openInputStream(uri)
                if (`is` != null) {
                    // возможный OutOfMemory при decodeStream без InJustDecodeBounds
                    val pictureBitmap = BitmapFactory.decodeStream(`is`)
                    realPath = getDataColumn(context, getImageUri(context, pictureBitmap))
                }
            } catch (e: FileNotFoundException) {
                Logger.e(e)
            }

        } else {
            realPath = getDataColumn(context, uri)
        }
        return realPath
    }

    @Deprecated("use getRealPath")
    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "img", null)
        return Uri.parse(path)
    }

    @Deprecated("use isGooglePhotosDocument")
    private fun isGooglePhotos(uri: Uri) = uri.toString().startsWith("content://com.google.android.apps.photos.content") && uri.toString().contains("mediakey")
}