/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.utilktx.ktx.ui.activity

import android.app.Activity

import android.graphics.Rect
import ru.surfstudio.android.utilktx.ktx.ui.context.getDisplayMetrics
import ru.surfstudio.android.utilktx.util.KeyboardUtil

/**
 * Extension-методы для Activity
 */

/**
 * Листенер на скрытие / появление клавиатуры
 * Осторожно! Может реагировать не только на события открытия/скрытия клавиатуры
 */
fun Activity.keyboardVisibilityToggleListener(listener: (Boolean) -> Unit) {
    val rootView = window.decorView

    rootView.viewTreeObserver
            .addOnGlobalLayoutListener {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                listener(keypadHeight > screenHeight * 0.15)
            }
}

/**
 * Скрытие экранной клавиатуры
 */
fun Activity.hideKeyboard() {
    KeyboardUtil.hideSoftKeyboard(this)
}

/**
 * Возвращает ширину или высоту экрана
 */
fun Activity.getDisplayParam(param: DisplayParam): Int {
    val metrics = getDisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)

    return when (param) {
        DisplayParam.WIDTH -> metrics.widthPixels
        DisplayParam.HEIGHT -> metrics.heightPixels
    }
}