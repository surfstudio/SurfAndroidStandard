package ru.surfstudio.android.utilktx.ktx.attr

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.annotation.StyleableRes
import android.support.v4.content.ContextCompat

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