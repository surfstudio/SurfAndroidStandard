package ru.surfstudio.android.picturechooser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import io.reactivex.Observable
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.picturechooser.exceptions.ActionInterruptedException

/**
 * Позволяет получить одно или несколько изображений через сторонее приложение
 */
class GalleryPictureProvider(private val activityNavigator: ActivityNavigator,
                             private val activity: Activity) {

    fun openGalleryForSingleImage(): Observable<String> {
        val route = GallerySingleImageRoute()

        val result = activityNavigator.observeResult<String>(route)
                .flatMap { result ->
                    if (result.isSuccess) {
                        Observable.just(result.data)
                    } else {
                        Observable.error(ActionInterruptedException())
                    }
                }

        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForMultipleImage(): Observable<List<String>> {
        val route = GalleryMultipleImageRoute()

        val result = activityNavigator.observeResult<ArrayList<String>>(route)
                .flatMap { result ->
                    if (result.isSuccess) {
                        Observable.just(result.data)
                    } else {
                        Observable.error(ActionInterruptedException())
                    }
                }
                .map { t -> t as List<String> }

        activityNavigator.startForResult(route)
        return result
    }

    private inner class GallerySingleImageRoute : ActivityWithResultRoute<String>() {
        override fun prepareIntent(context: Context?): Intent {
            return Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT, EXTERNAL_CONTENT_URI)
                    .apply { type = "image/*" },
                    activity.getString(R.string.choose_app))
        }

        override fun parseResultIntent(intent: Intent?): String? {
            return if (intent != null && intent.data != null) {
                intent.data!!.getRealPath()
            } else null
        }
    }

    private inner class GalleryMultipleImageRoute : ActivityWithResultRoute<ArrayList<String>>() {
        override fun prepareIntent(context: Context?): Intent {
            return Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT, EXTERNAL_CONTENT_URI)
                    .apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    },
                    activity.getString(R.string.choose_app))
        }

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? {
            return if (intent != null && intent.data != null) {
                arrayListOf(intent.data.getRealPath())
            } else if (intent != null && intent.clipData != null) {
                with(intent.clipData) {
                    (0 until itemCount).mapTo(ArrayList()) { getItemAt(it).uri.getRealPath() }
                }
            } else {
                null
            }
        }
    }

    private fun Uri.getRealPath(): String {
        val result: String
        val cursor = activity.contentResolver
                .query(this, null, null, null, null)
        if (cursor == null) {
            result = this.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = if (idx > -1) {
                cursor.getString(idx)
            } else {
                this.path
            }
            cursor.close()
        }
        return result
    }
}
