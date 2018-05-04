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
import android.view.Gravity
import android.view.View

/**
 * Интерфейс контроллера отображения сообщений
 * Максимальное количество линий задается в integers:design_snackbar_text_max_lines
 */
private val DEFAULT_TOAST_GRAVITY = Gravity.BOTTOM

interface MessageController {

    fun show(message: String,
             @ColorRes backgroundColor: Int? = null,
             @StringRes actionStringId: Int? = null,
             @ColorRes buttonColor: Int? = null,
             listener: (view: View) -> Unit = {})

    fun show(@StringRes stringId: Int,
             @ColorRes backgroundColor: Int? = null,
             @StringRes actionStringId: Int? = null,
             @ColorRes buttonColor: Int? = null,
             listener: (view: View) -> Unit = {})


    fun showToast(@StringRes stringId: Int,
                  gravity: Int = DEFAULT_TOAST_GRAVITY)

    fun showToast(message: String,
                  gravity: Int = DEFAULT_TOAST_GRAVITY)
}