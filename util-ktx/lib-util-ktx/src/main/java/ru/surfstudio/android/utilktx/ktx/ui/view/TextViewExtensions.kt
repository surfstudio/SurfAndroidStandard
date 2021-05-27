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
package ru.surfstudio.android.utilktx.ktx.ui.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IntegerRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import ru.surfstudio.android.utilktx.ktx.text.PHONE_NUMBER_CHARS
import ru.surfstudio.android.utilktx.util.KeyboardUtil
import ru.surfstudio.android.utilktx.util.SdkUtils

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
 * Установка стиля [TextView].
 *
 * Автоматически выбирается наиболее оптимальный способ сделать это в зависимости от версии системы.
 */
fun TextView.setTextAppearanceStyle(@StyleRes styleResId: Int) {
    if (SdkUtils.isAtLeastMarshmallow()) {
        setTextAppearance(styleResId)
    } else {
        @Suppress("DEPRECATION")
        setTextAppearance(this.context, styleResId)
    }
}

/**
 * Функция для копирования текста из TextView в буфер обмена
 */
fun TextView.copyTextToClipboard() {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
}

/**
 * Установка лимита на количество символов допустимых к набору в [EditText].
 *
 * @param length ссылка [@IntegerRes] на целочисленное значение, соответствующее максимальному
 * количеству символов, допустимых к набору в [EditText].
 */
fun EditText.setMaxLength(@IntegerRes length: Int) {
    val inputTextFilter = InputFilter.LengthFilter(context.resources.getInteger(length))

    filters = filters
            .filterNot { it is InputFilter.LengthFilter }
            .toTypedArray()
            .plus(inputTextFilter)
}

/**
 * Установка текста + выключение подчеркивания
 */
fun EditText.setText(string: String?, disableUnderline: Boolean) {
    setText(string)
    removeUnderline(disableUnderline)
}

/**
 * Устанавливает курсор в конец EditText'а
 */
fun EditText.selectionToEnd() {
    if (text.isNotEmpty()) {
        setSelection(text.length)
    }
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
 */
fun EditText.allowJustText() {
    val notJustText: (Char) -> Boolean = {
        !Character.isLetter(it) && !Character.isSpaceChar(it)
    }
    restrictMatch(notJustText)
}

/**
 * Разрешение ввода текста в [EditText] по предикату
 *
 * @param predicate предикат с условием разрешенных к вводу символов
 */
fun EditText.allowMatch(predicate: (Char) -> Boolean) {
    return this.restrictMatch { !predicate(it) }
}

/**
 * Запрет ввода в [EditText] по предикату
 *
 * @param predicate предикат, ограничивающий возможность ввода текста
 */
fun EditText.restrictMatch(predicate: (Char) -> Boolean) {
    val inputTextFilter = InputFilter { source, start, end, _, _, _ ->
        if ((start until end).any { predicate(source[it]) }) {
            source.trim { predicate(it) }.toString()
        } else {
            null
        }
    }
    this.filters = arrayOf(inputTextFilter).plus(filters)
}

/**
 * Позволяет вводить только телефонный номер
 */
fun EditText.allowJustPhoneNumber() {
    val inputTextFilter = InputFilter { source, _, _, _, _, _ ->
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

/**
 * Extension-функция предоставляющая фокус на [EditText] и устанавливающая курсор в конец
 * введённого текста.
 */
fun EditText.setFocusAndCursorToEnd() {
    requestFocus()
    KeyboardUtil.showKeyboard(this)
    selectionToEnd()
}