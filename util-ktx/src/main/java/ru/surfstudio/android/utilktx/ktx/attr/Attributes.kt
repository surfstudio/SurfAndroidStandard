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

const val NOT_ASSIGNED_DIMEN = 0    //заглушка для незаданного dimen-атрибута

/**
 * Безопасное извлечение dimen-атрибута или 0, если атрибут не задан.
 *
 * @param styleableResId ссылка на извлекаемый атрибут.
 */
fun TypedArray.obtainDimensionPixelAttribute(@StyleableRes styleableResId: Int) =
        this.getDimensionPixelOffset(styleableResId, NOT_ASSIGNED_DIMEN)

const val NOT_ASSIGNED_RESOURCE = -1         //заглушка для незаданного ссылочного атрибута

/**
 * Безопасное извлечение ссылки на ресурс из атрибута или -1, если атрибут не задан.
 *
 * @param styleableResId ссылка на извлекаемый атрибут.
 */
fun TypedArray.obtainResourceIdAttribute(@StyleableRes styleableResId: Int) =
        this.getResourceId(styleableResId, NOT_ASSIGNED_RESOURCE)