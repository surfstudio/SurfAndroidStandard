package ru.surfstudio.android.picturechooser.destination

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import ru.surfstudio.android.picturechooser.exceptions.ExternalStorageException
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Интерфейс для генерации [Uri]
 */
interface PictureDestinationProvider {
    /**
     * @return [Uri] изображения
     */
    fun provideDestination(): Uri

    /**
     * Метод удаления [uri]
     */
    fun deleteDestination(uri: Uri)
}

/**
 * Реализация [PictureDestinationProvider] для генерации [Uri] с помощью [contentResolver]
 * Таблица определяется через [pictureTableProvider]
 * Значения для столбцов задаются через [contentValuesGenerator]
 */
class ContentResolverUriProvider(
        private val contentResolver: ContentResolver,
        private val pictureTableProvider: PictureTableProvider = DefaultPictureTableProvider(),
        private val contentValuesGenerator: PictureContentValuesGenerator = PictureContentValuesGenerator
) : PictureDestinationProvider {

    override fun provideDestination(): Uri {
        return contentResolver.insert(
                pictureTableProvider.providePictureTable(),
                contentValuesGenerator.generateContentValues()
        ) ?: error("Failed generate image uri for new photo")
    }

    override fun deleteDestination(uri: Uri) {
        val countOfDeletionRows = contentResolver.delete(uri, null, null)
        if (countOfDeletionRows <= 0) {
            error("Uri $uri delete failed")
        }
    }
}

/**
 * Генератор значений для столбцов в таблице
 */
interface PictureContentValuesGenerator {

    @CallSuper
    fun generateContentValues(): ContentValues {
        return ContentValues()
    }

    companion object : PictureContentValuesGenerator
}

/**
 * Реализация [PictureContentValuesGenerator] для версий Android либо равных [Build.VERSION_CODES.Q],
 * либо последующих
 */
@RequiresApi(Build.VERSION_CODES.Q)
open class RelativePathPictureContentValuesGenerator(
        private val pictureNameGenerator: PictureNameGenerator = PictureNameGenerator,
        private val pictureFolderGenerator: PictureFolderGenerator = PictureFolderGenerator
) : PictureContentValuesGenerator {

    override fun generateContentValues(): ContentValues {
        return super.generateContentValues().apply {
            pictureNameGenerator.generatePictureName().takeIf { pictureName ->
                pictureName.isNotEmpty()
            }?.let { pictureName ->
                put(MediaStore.Images.Media.DISPLAY_NAME, pictureName)
            }
            pictureFolderGenerator.generatePictureFolderPath().takeIf { pictureRelativePath ->
                pictureRelativePath.isNotEmpty()
            }?.let { pictureRelativePath ->
                put(MediaStore.Images.Media.RELATIVE_PATH, pictureRelativePath)
            }
        }
    }
}

/**
 * Реализация [PictureContentValuesGenerator] для версий Android до [Build.VERSION_CODES.Q],
 * для обратной совместимости
 */
@Deprecated(
        message = "Don't use this class on Android 11 and higher, " +
                "instead use {@link RelativePathPictureContentValuesGenerator} " +
                "or create your own implementation of {@link PictureContentValuesGenerator}"
)
class AbsolutePathContentValuesGenerator(
        private val pictureNameGenerator: PictureNameGenerator = PictureNameGenerator,
        private val pictureFolderGenerator: PictureFolderGenerator = PictureFolderGenerator,
        private val pictureBaseDirectoryProvider: PictureBaseDirectoryProvider = DefaultPictureBaseDirectoryProvider
) : PictureContentValuesGenerator {

    override fun generateContentValues(): ContentValues {
        return super.generateContentValues().apply {
            val absolutePathToPicture = generateAbsolutePathToPicture()
            put(MediaStore.Images.Media.DATA, absolutePathToPicture)
        }
    }

    private fun generateAbsolutePathToPicture(): String {
        val pictureBaseDirectory = pictureBaseDirectoryProvider.provideBaseDirectory()
        val pictureRelativePath = pictureFolderGenerator.generatePictureFolderPath()
        val pictureName = pictureNameGenerator.generatePictureName()
        val pictureDirection = File(pictureBaseDirectory, pictureRelativePath)
        if (!pictureDirection.exists()) {
            if (!pictureDirection.mkdirs()) {
                throw ExternalStorageException("Failed to create directory ${pictureDirection.absolutePath}")
            }
        }
        val pictureFile = File(pictureDirection, pictureName)
        return pictureFile.absolutePath
    }

    /**
     * Интерфейс для предоставления базой директории для нового изображения
     */
    interface PictureBaseDirectoryProvider {
        fun provideBaseDirectory(): File
    }

    @Deprecated(
            message = "On Android 10 (API level 29) and higher need use {@link Context#getExternalFilesDir(String)} " +
                    "or use {@link RelativePathPictureContentValuesGenerator}"
    )
    private object DefaultPictureBaseDirectoryProvider : PictureBaseDirectoryProvider {
        override fun provideBaseDirectory(): File {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        }
    }
}

internal class DefaultPictureTableProvider : PictureTableProvider {

    override fun providePictureTable(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
}

/**
 * Интерфейс для генерации имени нового изображения
 */
interface PictureNameGenerator {
    /**
     * @return имя нового изображения
     */
    fun generatePictureName(): String {
        val date = Date()
        date.time = System.currentTimeMillis() + Random().nextInt(1000) + 1
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date)
        return "IMG_$timeStamp.jpg"
    }

    companion object : PictureNameGenerator
}

/**
 * Интерфейс для генерации относительного пути до нового изображения
 */
interface PictureFolderGenerator {
    /**
     * @return относительный путь для нового изображения
     */
    fun generatePictureFolderPath(): String {
        return ""
    }

    companion object : PictureFolderGenerator
}

/**
 * Интерфейс для предоставления таблицы, в которой будет произведена запись о новом изображении
 */
interface PictureTableProvider {
    fun providePictureTable(): Uri
}