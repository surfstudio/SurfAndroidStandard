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


import androidx.annotation.StyleRes
import android.telephony.PhoneNumberUtils
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.text.style.URLSpan
import android.widget.TextView
import ru.surfstudio.android.utilktx.ktx.text.DECIMAL_VALUE_FORMAT
import ru.surfstudio.android.utilktx.ktx.text.VALID_VALUE
import ru.surfstudio.android.utilktx.ktx.text.fractionalFormat
import ru.surfstudio.android.utilktx.ktx.text.wholeFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * Вспомогательные методы работы со строками
 */
object StringUtil {

    /**
     * Убирает подчеркивание у текста
     */
    fun removeUnderlines(text: Spannable) {
        val spans = text.getSpans<URLSpan>(0, text.length, URLSpan::class.java)

        for (span in spans) {
            val start = text.getSpanStart(span)
            val end = text.getSpanEnd(span)
            text.removeSpan(span)
            text.setSpan(URLSpanNoUnderline(span.url), start, end, 0)
        }
    }

    /**
     * Удалить нули в начале строки
     */
    fun removeLeadingZeros(source: String): String {
        return source.replaceFirst("^0+(?!$)".toRegex(), "")
    }

    /**
     * Устанавливает строку и подстроку
     */
    fun setTextWithSubtitle(textView: TextView, text: String, subtitleText: String, @StyleRes subtitleStyleId: Int) {
        val builder = SpannableStringBuilder(text)
        builder.append("\n")
        builder.append(subtitleText)
        builder.setSpan(TextAppearanceSpan(textView.context, subtitleStyleId),
                text.length, text.length + subtitleText.length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = builder
    }


    /**
     * Форматирует число в строке
     */
    fun getSplitedNumber(value: Double): String {
        //Locale.US - чтобы на выходе была строка вида *.**, а не *,**
        if (value < VALID_VALUE) {
            return 0.toString()
        }

        val valueInString = String.format(Locale.US, DECIMAL_VALUE_FORMAT, value)
        val d = java.lang.Double.valueOf(valueInString)
        return getDecimalFormat(fractionalFormat).format(d)
    }

    fun getSplitedNumber(value: Int): String {
        return getSplitedNumber(value.toLong())
    }

    fun getSplitedNumber(value: Long): String {
        return if (value > 0) getDecimalFormat(wholeFormat).format(value) else 0.toString()
    }

    /**
     * Форматирование телефонного номера
     */
    fun formatPhone(source: String): String? {
        return if (SdkUtils.isAtLeastLollipop()) {
            PhoneNumberUtils.formatNumber(source, Locale.getDefault().country)
        } else {
            PhoneNumberUtils.formatNumber(source)
        }
    }

    private fun getDecimalFormat(pattern: String): DecimalFormat {
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.groupingSeparator = ' '
        symbols.decimalSeparator = ','
        return DecimalFormat(pattern, symbols)
    }
}