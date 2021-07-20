package ru.surfstudio.android.permission.sample.screen.main.camera

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

class CameraRoute : ActivityRoute() {

    override fun prepareIntent(context: Context): Intent {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        return Intent.createChooser(
            takePictureIntent,
            "Choose image"
        )
    }
}
