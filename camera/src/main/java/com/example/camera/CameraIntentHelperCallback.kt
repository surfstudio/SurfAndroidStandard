package ru.trinitydigital.poison.ui.util.camera

import android.net.Uri
import java.util.*


/**
 * интерфейс для результата получения фото с камеры
 */
interface CameraIntentHelperCallback {
    fun onPhotoUriFound(dateCameraIntentStarted: Date, photoUri: Uri?, rotateXDegrees: Int)

    fun deletePhotoWithUri(photoUri: Uri)

    fun onSdCardNotMounted()

    fun onCanceled()

    fun onCouldNotTakePhoto()

    fun onPhotoUriNotFound()

    fun logException(e: Exception)
}