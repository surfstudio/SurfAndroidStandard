package ru.surfstudio.standard.ui.util.camera

import android.provider.MediaStore
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.*
import javax.inject.Inject

/**
 * Сервис для получения картинок из галереи для экрана создания рецензии
 */
@PerScreen
class MediaStoreService @Inject
constructor(private val activityProvider: ActivityProvider) {

    /**
     * Получить отсортированные пути к картинкам из галлереи
     */
    //не добавляем некорректные изображения
    val allStorageImagesPath: Observable<ArrayList<String>>
        get() = Observable.create { subscriber ->
            val images = ArrayList<String>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT)
            val cursor = activityProvider.get().contentResolver.query(uri, projection, null, null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC")
            if (cursor != null) {
                val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                val columnIndexWidth = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)
                val columnIndexHeight = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT)
                while (cursor.moveToNext()) {
                    val width = cursor.getInt(columnIndexWidth)
                    val height = cursor.getInt(columnIndexHeight)
                    if (width > 0 && height > 0) {
                        val absolutePathOfImage = cursor.getString(columnIndexData)
                        images.add(absolutePathOfImage)
                    }
                }
                cursor.close()
            }
            subscriber.onNext(images)
            subscriber.onComplete()
        }
}