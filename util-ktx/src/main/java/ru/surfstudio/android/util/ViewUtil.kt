package ru.surfstudio.android.util

import android.content.Context
import android.util.TypedValue
import android.widget.EditText
import ru.surfstudio.android.ktx.ui.context.getDisplayMetrics
import ru.surfstudio.android.ktx.ui.textview.removeUnderline

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