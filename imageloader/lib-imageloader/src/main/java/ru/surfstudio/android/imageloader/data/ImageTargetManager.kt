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
package ru.surfstudio.android.imageloader.data

import android.content.Context
import android.view.View
import android.widget.ImageView

/**
 * Пакет с описанием целей загрузки изображений
 */
data class ImageTargetManager(
        private val context: Context,
        private val imageResourceManager: ImageResourceManager = ImageResourceManager(context),
        var targetView: View? = null    //целевая View
) {
    /**
     * Установка заглушки ошибки для [View]
     **/
    fun setErrorImage() {
        if (targetView is ImageView) {
            (targetView as ImageView).setImageResource(imageResourceManager.errorResId)
        } else {
            targetView?.setBackgroundResource(imageResourceManager.errorResId)
        }
    }
}