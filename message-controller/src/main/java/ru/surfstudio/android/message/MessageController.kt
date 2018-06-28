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
package ru.surfstudio.android.message

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.Gravity
import android.view.View
import android.widget.Toast

/**
 * Интерфейс контроллера отображения сообщений
 * Максимальное количество линий задается в integers:design_snackbar_text_max_lines
 */

interface MessageController {

    companion object {
        private const val DEFAULT_TOAST_GRAVITY = Gravity.BOTTOM
        private const val DEFAULT_TOAST_DURATION = Toast.LENGTH_LONG
        private const val DEFAULT_SNACK_DURATION = Snackbar.LENGTH_LONG
    }

    fun show(message: String,
             @ColorRes backgroundColor: Int? = null,
             @StringRes actionStringId: Int? = null,
             @ColorRes buttonColor: Int? = null,
             duration: Int = DEFAULT_SNACK_DURATION,
             listener: (view: View) -> Unit = {})

    fun show(@StringRes stringId: Int,
             @ColorRes backgroundColor: Int? = null,
             @StringRes actionStringId: Int? = null,
             @ColorRes buttonColor: Int? = null,
             duration: Int = DEFAULT_SNACK_DURATION,
             listener: (view: View) -> Unit = {})


    fun showToast(@StringRes stringId: Int,
                  gravity: Int = DEFAULT_TOAST_GRAVITY,
                  duration: Int = DEFAULT_TOAST_DURATION)

    fun showToast(message: String,
                  gravity: Int = DEFAULT_TOAST_GRAVITY,
                  duration: Int = Toast.LENGTH_LONG)
}