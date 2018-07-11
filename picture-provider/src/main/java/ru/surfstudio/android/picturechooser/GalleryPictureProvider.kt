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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
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
                intent.data!!.uriToFilePath()
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
                arrayListOf(intent.data.uriToFilePath())
            } else if (intent != null && intent.clipData != null) {
                with(intent.clipData) {
                    (0 until itemCount).mapTo(ArrayList()) { getItemAt(it).uri.uriToFilePath() }
                }
            } else {
                null
            }
        }
    }

    /**
     * Получить реальный путь до изображения
     */
    @SuppressLint("ObsoleteSdkInt")
    fun Uri.uriToFilePath(): String {
        return when {
            Build.VERSION.SDK_INT < 11 -> RealPathUtil.getRealPathFromUriApiBelow11(activity, this)
            Build.VERSION.SDK_INT < 19 -> RealPathUtil.getRealPathFromUriApi11to18(activity, this)
            else -> RealPathUtil.getRealPathFromUriApi19(activity, this)
        }
    }
}
