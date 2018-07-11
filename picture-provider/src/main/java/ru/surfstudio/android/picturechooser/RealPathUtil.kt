package ru.surfstudio.android.picturechooser

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.CursorLoader

/**
 * Утилита для получения реального пути до изображения
 */
object RealPathUtil {

    /**
     * Для SDK 19 и выше
     */
    fun getRealPathFromUriApi19(activity: Activity, uri: Uri): String {
        var result = ""
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            val wholeID = DocumentsContract.getDocumentId(uri)
            val splits = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (splits.size == 2) {
                val id = splits[1]
                val column = arrayOf(MediaStore.Images.Media.DATA)
                val sel = MediaStore.Images.Media._ID + "=?"
                val cursor = activity.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, arrayOf(id), null)
                val columnIndex = cursor!!.getColumnIndex(column[0])
                if (cursor.moveToFirst()) {
                    result = cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } else {
            result = uri.path
        }
        return result
    }

    /**
     * Для SDK от 11 до 18
     */
    @SuppressLint("NewApi")
    fun getRealPathFromUriApi11to18(context: Context, contentUri: Uri): String {
        var result = ""
        val media = arrayOf(MediaStore.Images.Media.DATA)
        val cursorLoader = CursorLoader(context, contentUri, media, null, null, null)
        val cursor = cursorLoader.loadInBackground()
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
        }
        return result
    }

    /**
     * Для SDK ниже 11
     */
    fun getRealPathFromUriApiBelow11(context: Context, contentUri: Uri): String {
        var result = ""
        val media = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri, media, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (cursor.moveToFirst()) {
            result = cursor.getString(columnIndex)
        }
        cursor.close()
        return result
    }
}