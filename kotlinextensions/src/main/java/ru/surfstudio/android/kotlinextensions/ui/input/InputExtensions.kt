package ru.surfstudio.android.kotlinextensions.ui.input

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.annotation.ColorRes
import android.support.annotation.IntegerRes
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import ru.surfstudio.android.kotlinextensions.ui.view.setVisibleOrGone
import ru.trinitydigital.poison.R
import ru.trinitydigital.poison.util.APOSTROPHE_STRING
import ru.trinitydigital.poison.util.HYPHEN_STRING
import ru.trinitydigital.poison.util.PHONE_NUMBER_CHARS

/**
 * Extension-функции для настроек ввода (установки лимитов, допустимых и запрещённых символов и т.д.)
 */

/**
 * Установка текста.
 *
 * Если текст пустой - [TextView] скрывается.
 */
fun TextView.setTextOrGone(text: String?) {
    setVisibleOrGone(text?.trim().isNullOrEmpty().not())
    setText(text)
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
 * Установка ограничения на допустимость набора в [EditText] только текста.
 *
 * Допустимые символы:
 *
 * * все литеры в любом регистре и любой локали;
 * * символ пробела: " ";
 * * символ дефиса: - ;
 * * символ апострофа: ' .
 */
fun EditText.allowJustText() {
    val notJustText: (Char) -> Boolean = {
        !Character.isLetter(it) &&
                !Character.isSpaceChar(it) &&
                it.toString() != HYPHEN_STRING &&
                it.toString() != APOSTROPHE_STRING
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
 * @param editText экземпляр [EditText]
 * @param textColorRes цвет текста
 * @param hintColorRes цвет хинта
 */
private fun EditText.setTextColors(@ColorRes textColorRes: Int, @ColorRes hintColorRes: Int) {
    editText.setTextColor(ContextCompat.getColor(editText.context, textColorRes))
    editText.setHintTextColor(ContextCompat.getColor(editText.context, hintColorRes))
}