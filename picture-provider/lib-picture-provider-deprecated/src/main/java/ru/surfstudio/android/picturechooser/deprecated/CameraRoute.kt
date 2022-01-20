package ru.surfstudio.android.picturechooser.deprecated

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import java.io.Serializable

/**
 * Базовый класс для создания маршрута экрана камеры
 */
@Deprecated("Prefer using new implementation")
open class BaseCameraRoute(val imageUri: Uri): ActivityWithResultRoute<ResultData>() {

    @CallSuper
    override fun prepareIntent(context: Context?): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
    }
}

/**
 * Стандартная реалзиация [BaseCameraRoute]
 */
@Deprecated("Prefer using new implementation")
class CameraRoute(
        imageUri: Uri,
        private val chooserTitle: String
) : BaseCameraRoute(imageUri) {

    override fun prepareIntent(context: Context?): Intent {
        return super.prepareIntent(context).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }.let {
            Intent.createChooser(it, chooserTitle)
        }
    }
}

@Deprecated("Prefer using new implementation")
data class ResultData(val photoPath: String) : Serializable