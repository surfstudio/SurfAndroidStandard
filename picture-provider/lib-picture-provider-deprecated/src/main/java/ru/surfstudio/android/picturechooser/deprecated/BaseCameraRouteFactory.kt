package ru.surfstudio.android.picturechooser.deprecated

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * Фабрика для создания [BaseCameraRoute]
 */
interface BaseCameraRouteFactory {
    fun create(uri: Uri): BaseCameraRoute

    companion object : BaseCameraRouteFactory {
        override fun create(uri: Uri): BaseCameraRoute {
            return BaseCameraRoute(uri)
        }
    }
}

/**
 * Реализация [BaseCameraRouteFactory] для создания [CameraRoute]
 */
class CameraRouteFactory(
        private val chooserTitle: String
) : BaseCameraRouteFactory {

    override fun create(uri: Uri): BaseCameraRoute {
        return CameraRoute(uri, chooserTitle)
    }
}

/**
 * Реализация [BaseCameraRouteFactory] для создания [CameraRoute] с помощью [File]
 */
internal class OldFileCameraRouteFactory(
        private val context: Context,
        private val authority: String,
        private val cameraRouteFactory: CameraRouteFactory = CameraRouteFactory(context.getString(R.string.choose_app))
) : BaseCameraRouteFactory by cameraRouteFactory {

    fun create(file: File): BaseCameraRoute {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, authority, file)
        } else {
            Uri.fromFile(file)
        }
        return create(uri)
    }
}