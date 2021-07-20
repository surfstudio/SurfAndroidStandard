package ru.surfstudio.android.navigation.sample.app.screen.system

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.android.utilktx.util.SdkUtils
import java.io.File

class CameraRoute(
        override val uniqueId: String,
        private val chooserTitle: String = EMPTY_STRING,
        private val takenPhotoFile: File = File(EMPTY_STRING)
) : ActivityWithResultRoute<Boolean>() {

    override fun createIntent(context: Context): Intent {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            val imageUri = when {
                SdkUtils.isAtLeastNougat() -> FileProvider.getUriForFile(
                        context,
                        AppFileProvider.getAuthority(context),
                        takenPhotoFile
                )
                else -> Uri.fromFile(takenPhotoFile)
            }

            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        return Intent.createChooser(
                takePictureIntent,
                chooserTitle
        )
    }

    override fun parseResultIntent(resultCode: Int, resultIntent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
