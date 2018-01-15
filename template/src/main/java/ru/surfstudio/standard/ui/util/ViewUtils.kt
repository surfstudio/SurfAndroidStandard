package ru.surfstudio.standard.ui.util


import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.VectorDrawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText

/**
 * Общие утилиты для работы с View
 */
class ViewUtils private constructor() {
    init {
        throw IllegalStateException(ViewUtils::class.java!!.getName() + " не может иметь инстанс")
    }

    interface KeyboardVisibilityToggleListener {

        fun onKeyboardVisibilityToggle(visible: Boolean)
    }

    companion object {

        /**
         * Конвертация dp в пиксели
         */
        fun convertDpToPx(context: Context, dp: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics(context)).toInt()
        }

        /**
         * Конвертация пискелей в dp
         */
        fun convertPxToDp(context: Context, px: Int): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px.toFloat(), getDisplayMetrics(context))
        }

        fun keyboardVisibilityToggleListener(activity: Activity,
                                             listener: KeyboardVisibilityToggleListener) {
                val rootView = activity.window.decorView

                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                val screenHeight = rootView.height
                val keypadHeight = screenHeight - rect.bottom

                listener.onKeyboardVisibilityToggle(keypadHeight > screenHeight * 0.15)
        }

        internal fun getDisplayMetrics(context: Context): DisplayMetrics {
            return context.resources.displayMetrics
        }

        fun getBitmapFromDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap {
            val drawable = ContextCompat.getDrawable(context, drawableId)

            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            } else if (drawable is VectorDrawableCompat || drawable is VectorDrawable) {
                val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                return bitmap
            } else {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }

        /**
         * Возвращает ширину или высоту экрана
         */
        fun getDisplayParam(activity: Activity, param: DisplayParam): Int {
            val metrics = getDisplayMetrics(activity)
            activity.windowManager.defaultDisplay.getMetrics(metrics)

            when (param) {
                DisplayParam.WIDTH -> return metrics.widthPixels
                DisplayParam.HEIGHT -> return metrics.heightPixels
                else -> throw IllegalArgumentException("Unsupported DisplayParam: " + param.name)
            }
        }

        /**
         * Убирает подчеркивание нескольких EditText , если они дисэйблятся все вместе
         *
         * @param shouldDisabled - флаг отключения
         * @param eds            - varargs с EditText
         */
        fun removeUnderlineFromEditTexts(shouldDisabled: Boolean, vararg eds: EditText) {
            for (ed in eds) {
                removeUnderlineFromEditText(shouldDisabled, ed)
            }
        }

        /**
         * Убирает подчеркивание с конкретного EditText
         *
         * @param shouldDisabled - флаг отключения
         * @param editText
         */
        private fun removeUnderlineFromEditText(shouldDisabled: Boolean, editText: EditText) {
            if (shouldDisabled) {
                editText.background = null
            }
        }

        /**
         * Меняет цвет background у view
         */
        fun changeViewBackgroundColor(view: View, @ColorInt color: Int) {
            val background = view.background.mutate()
            if (background is ShapeDrawable) {
                background.paint.color = color
            } else if (background is GradientDrawable) {
                background.setColor(color)
            } else if (background is ColorDrawable) {
                background.color = color
            }
        }
    }
}