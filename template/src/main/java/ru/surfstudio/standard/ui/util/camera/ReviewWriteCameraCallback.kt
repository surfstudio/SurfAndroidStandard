package ru.surfstudio.standard.ui.util.camera

import android.net.Uri

interface ReviewWriteCameraCallback {
    fun onPhotoUriFound(photoUri: Uri)

    fun onError(message: String)
}