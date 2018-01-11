package ru.surfstudio.standard.ui.util.camera

import android.net.Uri

import java.util.Date

/**
 * Specifies the interface of a CameraIntentHelper request. The calling class has to implement the
 * interface in order to be notified when the request completes, either successfully or with an error.
 *
 * @author Ralf Gehrer <ralf></ralf>@ecotastic.de>
 */
interface CameraIntentHelperCallback {
    fun onPhotoUriFound(dateCameraIntentStarted: Date?, photoUri: Uri?, rotateXDegrees: Int)

    fun deletePhotoWithUri(photoUri: Uri)

    fun onSdCardNotMounted()

    fun onCanceled()

    fun onCouldNotTakePhoto()

    fun onPhotoUriNotFound()

    fun logException(e: Exception)
}