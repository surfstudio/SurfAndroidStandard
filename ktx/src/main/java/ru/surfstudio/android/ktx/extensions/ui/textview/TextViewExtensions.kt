package ru.surfstudio.android.ktx.extensions.ui.textview

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.annotation.ColorRes
import android.support.annotation.IntegerRes
import android.support.v4.content.ContextCompat
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import ru.surfstudio.android.core.ui.util.KeyboardUtil
import ru.surfstudio.android.ktx.extensions.ui.view.goneIf
import ru.surfstudio.android.ktx.extensions.text.PHONE_NUMBER_CHARS

/**
 * Extension-функции для настроек ввода (установки лимитов, допустимых и запрещённых символов и т.д.)
 */

/**
 * Установка текста.
 *
 * Если текст пустой - [TextView] скрывается.
 */
fun TextView.setTextOrGone(text: String?) {
    goneIf(text.isNullOrBlank())
    setText(text)
}

/**
 * Задать цвет Drawable у TextView
 */
fun TextView.setDrawableColor(color: Int) {
    this.compoundDrawablesRelative
            .filterNotNull()
            .forEach { it.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN) }
}

/**
 * Установка лимита на количество символов допустимых к набору в [EditText].
 *
 * @param length ссылка [@IntegerRes] на целочисленное значение, соответствующее максимальному
 * количеству символов, допустимых к набору в [EditText].
 */
fun EditText.setMaxLength(@IntegerRes length: Int) {
    val inputTextFilter = InputFilter.LengthFilter(context.resources.getInteger(length))
    this.filters = arrayOf<InputFilter>(inputTextFilter).plus(filters)
}

/**
 * Установка текста + выключение подчеркивания
 */
fun EditText.setText(string: String?, disableUnderline: Boolean) {
    setText(string)
    removeUnderline(disableUnderline)
}

/**
 * Убирает подчеркивание с конкретного EditText
 *
 * @param shouldDisabled - флаг отключения
 */
fun EditText.removeUnderline(disableUnderline: Boolean) {
    if (disableUnderline) {
        background = null
    }
}

/**
 * Возврат к дефолтному фону для EditText (возвращает подчеркивание)
 */
fun EditText.resetToDefaultBackground() {
    background = ContextCompat.getDrawable(context, android.R.drawable.edit_text)
}

/**
 * Установка ограничения на допустимость набора в [EditText] только текста.
 * TODO сделать обобщенный метод на запрет ввода определенных символов
 */
fun EditText.allowJustText() {
    val notJustText: (Char) -> Boolean = {
        !Character.isLetter(it) && !Character.isSpaceChar(it)
    }
    val inputTextFilter = InputFilter { source, start, end, _, _, _ ->
        if ((start until end).any { notJustText(source[it]) })
            source.trim { notJustText(it) }.toString() else null
    }
    this.filters = arrayOf(inputTextFilter).plus(filters)
}

/**
 * Позволяет вводить только телефонный номер
 */
fun EditText.allowJustPhoneNumber() {
    val inputTextFilter = InputFilter { source, start, end, _, _, _ ->
        val inputString = source.toString()
        val inputStringLength = inputString.length

        //удаляем последний введеный символ если он не цифра или второй + в строке
        if ((inputStringLength > 1 && inputString.endsWith("+")) ||
                (inputStringLength > 0 && !PHONE_NUMBER_CHARS.contains(inputString[inputStringLength - 1]))) {
            source.removeRange(inputStringLength - 1, inputStringLength)
        } else {
            null
        }
    }

    this.filters = arrayOf(inputTextFilter).plus(filters)
}

/**
 * Смена цвета элементов [EditText]
 *
 * @param textColorRes цвет текста
 * @param hintColorRes цвет хинта
 */
fun EditText.setTextColors(@ColorRes textColorRes: Int, @ColorRes hintColorRes: Int) {
    this.setTextColor(ContextCompat.getColor(this.context, textColorRes))
    this.setHintTextColor(ContextCompat.getColor(this.context, hintColorRes))
}

/**
 * Показать экранную клавиатуру.
 *
 *
 * Клавиатура открывается с нужным для указанного [EditText] [android.text.InputType].
 */

fun EditText.showKeyboard() {
    KeyboardUtil.showKeyboard(this)
}
