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
package ru.surfstudio.android.utilktx.util

import android.content.Context
import android.util.TypedValue
import android.widget.EditText
import ru.surfstudio.android.utilktx.ktx.ui.context.getDisplayMetrics
import ru.surfstudio.android.utilktx.ktx.ui.view.removeUnderline

/**
 * Утилитные методы для работы с вью
 */
object ViewUtil {
    /**
     * Конвертация dp в пиксели
     */
    fun convertDpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.getDisplayMetrics()
        ).toInt()
    }

    /**
     * Конвертация пискелей в dp
     */
    fun convertPxToDp(context: Context, px: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            px.toFloat(),
            context.getDisplayMetrics()
        )
    }

    /**
     * Убирает подчеркивание нескольких EditText , если они дисэйблятся все вместе
     *
     * @param shouldDisabled - флаг отключения
     * @param eds            - varargs с EditText
     */
    fun removeUnderlineFromEditTexts(shouldDisabled: Boolean, vararg eds: EditText) {
        for (ed in eds) {
            ed.removeUnderline(shouldDisabled)
        }
    }

}