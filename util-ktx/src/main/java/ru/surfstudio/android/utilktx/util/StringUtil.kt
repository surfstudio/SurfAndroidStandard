package ru.surfstudio.android.utilktx.util


import android.support.annotation.StyleRes
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
        return if (value > 0) getDecimalFormat(wholeFormat).format(value.toLong()) else 0.toString()
    }

    /**
     * Форматирование телефонного номера
     */
    fun formatPhone(source: String): String? {
        return if (SdkUtils.isAtLeastLollipop) {
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