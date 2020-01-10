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
package ru.surfstudio.android.utilktx.ktx.attr

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat

/**
 * Extension-функции для работы с кастомными атрибутами
 */

/**
 * Безопасное извлечение Drawable из кастомного атрибута по ссылке на ресурс.
 *
 * @param context контекст
 * @param styleableResId ссылка на извлекаемый атрибут
 */
fun TypedArray.obtainDrawableAttribute(context: Context, @StyleableRes styleableResId: Int): Drawable? =
        this.getResourceId(styleableResId, 0).let {
            return if (it == 0) {
                null
            } else {
                ContextCompat.getDrawable(context, it)
            }
        }

/**
 * Безопасное извлечение строки из кастомного атрибута по ссылке на ресурс.
 *
 * @param styleableResId ссылка на извлекаемый атрибут
 */
fun TypedArray.obtainStringAttribute(@StyleableRes styleableResId: Int) =
        this.getString(styleableResId) ?: ""

const val NOT_ASSIGNED_DIMEN = 0    //заглушка для незаданного dimen-атрибута

/**
 * Безопасное извлечение dimen-атрибута или 0, если атрибут не задан.
 *
 * @param styleableResId ссылка на извлекаемый атрибут
 * @param defValue значение по умолчанию
 */
fun TypedArray.obtainDimensionPixelAttribute(@StyleableRes styleableResId: Int, defValue: Int = NOT_ASSIGNED_DIMEN) =
        this.getDimensionPixelOffset(styleableResId, defValue)

const val NOT_ASSIGNED_RESOURCE = -1         //заглушка для незаданного ссылочного атрибута

/**
 * Безопасное извлечение ссылки на ресурс из атрибута или -1, если атрибут не задан.
 *
 * @param styleableResId ссылка на извлекаемый атрибут.
 */
fun TypedArray.obtainResourceIdAttribute(@StyleableRes styleableResId: Int) =
        this.getResourceId(styleableResId, NOT_ASSIGNED_RESOURCE)

/**
 * Безопасное извлечение значения цветового атрибута. Если атрибут не задан - цвет чёрный.
 *
 * @param styleableResId ссылка на извлекаемый атрибут.
 */
@ColorInt
fun TypedArray.obtainColorAttribute(@StyleableRes styleableResId: Int) =
        this.getColor(styleableResId, Color.BLACK)