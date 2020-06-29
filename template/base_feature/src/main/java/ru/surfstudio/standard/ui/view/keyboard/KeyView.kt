package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.ui.view.keyboard.keys.BaseIconKey
import ru.surfstudio.standard.ui.view.keyboard.keys.BaseTextKey
import ru.surfstudio.standard.ui.view.keyboard.keys.Key

/**
 * View для отображения кнопки на кастомной калавиатуре. Умеет рисовать текст или иконку
 */
class KeyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0,
        defStyleRes: Int = R.style.KeyViewStyle
) : LableView(context, attrs, defStyleAttrs, defStyleRes) {

    var key: Key? = null
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (key) {
            is BaseTextKey -> draw(canvas, (key as BaseTextKey).text)
            is BaseIconKey -> draw(canvas, (key as BaseIconKey).icon)
            //ignore EmptyKey
        }
    }
}