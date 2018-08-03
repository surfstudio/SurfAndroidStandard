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
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityWithResultRoute
import ru.surfstudio.android.picturechooser.exceptions.ActionInterruptedException
import java.io.Serializable

/**
 * Позволяет получить одно или несколько изображений через сторонее приложение
 */
class GalleryPictureProvider(private val activityNavigator: ActivityNavigator,
                             private val activity: Activity) {

    //region Функции для выбора одного изображения из галереи
    fun openGalleryForSingleImage(): Observable<String> {
        val route = GallerySingleImageRoute()

        val result = parseSingleScreenResult(route)

        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForSingleImageUri(): Observable<String> {
        val route = GallerySingleImageUriRoute()

        val result = parseSingleScreenResult(route)

        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForSingleImageUriResult(): Observable<UriResult> {
        val route = GallerySingleImageUriResultRoute()

        val result = parseSingleScreenResult(route)

        activityNavigator.startForResult(route)
        return result
    }
    //endregion

    //region Функции для выбора нескольких изображений из галереи
    fun openGalleryForMultipleImage(): Observable<List<String>> {
        val route = GalleryMultipleImageRoute()

        val result = parseMultipleScreenResult(route)

        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForMultipleImageUri(): Observable<List<String>> {
        val route = GalleryMultipleImageUriRoute()

        val result = parseMultipleScreenResult(route)

        activityNavigator.startForResult(route)
        return result
    }

    fun openGalleryForMultipleImageUriResult(): Observable<List<UriResult>> {
        val route = GalleryMultipleImageUriResultRoute()

        val result = parseMultipleScreenResult(route)

        activityNavigator.startForResult(route)
        return result
    }
    //endregion

    private fun <T : Serializable> parseSingleScreenResult(route: ActivityWithResultRoute<T>): Observable<T> {
        return activityNavigator.observeResult<T>(route)
                .flatMap { parseScreenResult(it) }
    }

    private fun <T : Serializable> parseMultipleScreenResult(route: ActivityWithResultRoute<ArrayList<T>>): Observable<List<T>> {
        return activityNavigator.observeResult<ArrayList<T>>(route)
                .flatMap { parseScreenResult(it) }
                .map { it as List<T> }
    }

    private fun <T : Serializable> parseScreenResult(screenResult: ScreenResult<T>,
                                                     throwable: () -> Throwable = { ActionInterruptedException() }): Observable<T> {
        return if (screenResult.isSuccess) {
            Observable.just(screenResult.data)
        } else {
            Observable.error(throwable)
        }
    }

    //region Роутеры для выбора одного изображения из галереи
    /**
     * Роутер, возвращащий путь к изображению
     */
    private inner class GallerySingleImageRoute : ActivityWithResultRoute<String>() {

        override fun prepareIntent(context: Context?) = getIntentForSingleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): String? {
            return parseSingleResultIntent(intent) { it.data.getRealPath() }
        }
    }

    /**
     * Роутер, возвращающий Uri изображения, преобразованный в String
     */
    private inner class GallerySingleImageUriRoute : ActivityWithResultRoute<String>() {

        override fun prepareIntent(context: Context?) = getIntentForSingleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): String? {
            return parseSingleResultIntent(intent) { it.data.toString() }
        }
    }

    /**
     * Роутер, возвращающий класс-обертку над Uri изображения
     */
    private inner class GallerySingleImageUriResultRoute : ActivityWithResultRoute<UriResult>() {

        override fun prepareIntent(context: Context?) = getIntentForSingleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): UriResult? {
            return parseSingleResultIntent(intent) { UriResult(it.data) }
        }
    }
    //endregion

    //region Роутеры для выбора нескольких изображений из галереи
    /**
     * Роутер, возвращающий список путей к выбранным изображениям
     */
    private inner class GalleryMultipleImageRoute : ActivityWithResultRoute<ArrayList<String>>() {

        override fun prepareIntent(context: Context?) = getIntentForMultipleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? {
            return parseMultipleResultIntent(intent) { it.getRealPath() }
        }
    }

    /**
     * Роутер, возвращающий список Uri выбранных изображений, преобразованных в String
     */
    private inner class GalleryMultipleImageUriRoute : ActivityWithResultRoute<ArrayList<String>>() {

        override fun prepareIntent(context: Context?) = getIntentForMultipleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): ArrayList<String>? {
            return parseMultipleResultIntent(intent) { it.toString() }
        }
    }

    /**
     * Роутер, возвращающий список элементов типа класса-обертки над Uri выбранных изображений
     */
    private inner class GalleryMultipleImageUriResultRoute : ActivityWithResultRoute<ArrayList<UriResult>>() {

        override fun prepareIntent(context: Context?) = getIntentForMultipleImageFromGallery()

        override fun parseResultIntent(intent: Intent?): ArrayList<UriResult>? {
            return parseMultipleResultIntent(intent) { UriResult(it) }
        }
    }
    //endregion

    /**
     * Класс-обертка для возвращения Uri с помощью ActivityWithResultRoute
     */
    data class UriResult(val uri: Uri) : Serializable

    //region Вспомогательные функции для роутеров
    private fun getIntentForSingleImageFromGallery(): Intent {
        return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    private fun getIntentForMultipleImageFromGallery(): Intent {
        return Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI)
                .apply {
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
    }

    private fun <T : Serializable> parseSingleResultIntent(intent: Intent?,
                                                           parseIntent: (intent: Intent) -> T): T? {
        return if (intent != null && intent.data != null) {
            parseIntent(intent)
        } else {
            null
        }

    }

    private fun <T : Serializable> parseMultipleResultIntent(intent: Intent?,
                                                             parseUri: (uri: Uri) -> T): ArrayList<T>? {
        return when {
            intent == null -> null
            intent.clipData != null -> with(intent.clipData) {
                (0 until itemCount).mapTo(ArrayList()) { parseUri(getItemAt(it).uri) }
            }
            intent.data != null -> arrayListOf(parseUri(intent.data))
            else -> null
        }
    }
    //endregion

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
