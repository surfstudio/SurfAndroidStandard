/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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

    fun openGalleryForSingleImageUri(): Observable<String> {
        val route = GallerySingleImageUriRoute()

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
            return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }

        override fun parseResultIntent(intent: Intent?): String? {
            return if (intent != null && intent.data != null) {
                intent.data!!.getRealPath()
            } else null
        }
    }

    private inner class GallerySingleImageUriRoute : ActivityWithResultRoute<String>() {
        override fun prepareIntent(context: Context?): Intent {
            return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }

        override fun parseResultIntent(intent: Intent?): String? {
            return if (intent != null && intent.data != null) {
                intent.data!!.toString()
            } else null
        }
    }

    private inner class GalleryMultipleImageRoute : ActivityWithResultRoute<ArrayList<String>>() {
        override fun prepareIntent(context: Context?): Intent {
            return Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI)
                    .apply {
                        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    }
        }

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? {
            return if (intent != null && intent.clipData != null) {
                with(intent.clipData) {
                    (0 until itemCount).mapTo(ArrayList()) { getItemAt(it).uri.getRealPath() }
                }
            } else if (intent != null && intent.data != null) {
                arrayListOf(intent.data.getRealPath())
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
