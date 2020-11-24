package ru.surfstudio.android.picturechooser

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.CallSuper
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import java.text.SimpleDateFormat
import java.util.*

/**
 * Интерфейс для предоставления расположения нового изображения
 */
interface PictureDestinationProvider {
    /**
     * @return [Uri], который будет представлять расположение нового изображения
     */
    fun providePictureDestination(): Uri
}

/**
 * Реализация [PictureDestinationProvider], которая в качестве места расположения нового изображения
 * будет использовать [MediaStore.Images.Media.EXTERNAL_CONTENT_URI]
 */
open class ExternalStorageUriProvider(
        private val activityProvider: ActivityProvider,
        private val pictureNameGenerator: PictureNameGenerator = SimplePictureNameGenerator()
): PictureDestinationProvider {

    private val currentActivity get() = activityProvider.get()

    override fun providePictureDestination(): Uri {
        val values = createContentValues()
        return currentActivity.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        ) ?: error("Failed generate image uri for new photo")
    }

    /**
     * @return [ContentValues] для создания [Uri] для нового изображения
     */
    @CallSuper
    protected open fun createContentValues(): ContentValues {
        return ContentValues().apply {
            val pictureName = pictureNameGenerator.generatePictureName()
            put(MediaStore.Images.Media.TITLE, pictureName)
        }
    }
}

/**
 * Интерфейс для генерации имени нового изображения
 */
interface PictureNameGenerator {
    /**
     * @return имя нового изображения
     */
    fun generatePictureName(): String
}

class SimplePictureNameGenerator: PictureNameGenerator {
    /**
     * @return имя нового изображения в формате IMG_yyyyMMdd_HHmmss_SSS.jpg
     */
    override fun generatePictureName(): String {
        val date = Date()
        date.time = System.currentTimeMillis() + Random().nextInt(1000) + 1
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date)
        return "IMG_$timeStamp.jpg"
    }
}