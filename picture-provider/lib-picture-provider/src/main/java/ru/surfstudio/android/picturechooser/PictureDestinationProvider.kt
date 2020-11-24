package ru.surfstudio.android.picturechooser

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import java.text.SimpleDateFormat
import java.util.*

/**
 * Интерфейс для предоставления расположения фотографии
 */
interface PictureDestinationProvider {
    fun providePictureDestination(): Uri
}

open class PictureUriProvider(
        private val activityProvider: ActivityProvider,
        private val pictureNameGenerator: PictureNameGenerator = PictureNameGenerator()
): PictureDestinationProvider {

    private val currentActivity get() = activityProvider.get()

    override fun providePictureDestination(): Uri {
        val values = createContentValues()
        return currentActivity.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        ) ?: error("Failed generate image uri for new photo")
    }

    @CallSuper
    protected open fun createContentValues(): ContentValues {
        return ContentValues().apply {
            val pictureName = pictureNameGenerator.generatePictureName()
            put(MediaStore.Images.Media.TITLE, pictureName)
        }
    }
}

class PictureNameGenerator {
    /**
     * @return имя для фото
     */
    open fun generatePictureName(): String {
        val date = Date()
        date.time = System.currentTimeMillis() + Random().nextInt(1000) + 1
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date)
        return "IMG_$timeStamp.jpg"
    }
}