package ru.surfstudio.standard.ui.util


import android.support.annotation.StyleRes
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.text.style.URLSpan
import android.widget.TextView

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Вспомогательные методы работы со строками
 */
object StringUtil {

    private val DECIMAL_VALUE_FORMAT = "%.2f"
    private val wholeFormat = "#,###"
    private val fractionalFormat = "###,###.##"
    private val VALID_VALUE = 0.00000001

    private val symbols = arrayOf<CharSequence>("&", "<", ">", "...", "™")
    private val tags = arrayOf<CharSequence>("&amp;", "&lt;", "&gt;", "&hellip;", "&trade;")

    //todo
//    fun htmlSimpleDecode(htmlText: String): String {
//        var decoded = htmlText
//        for (tagIndex in tags.indices) {
//            decoded = decoded.replace(tags[tagIndex], symbols[tagIndex])
//        }
//        return decoded
//    }

    /**
     * Убирает подчеркивание у текста
     */
    fun removeUnderlines(text: Spannable) {
        val spans = text.getSpans<URLSpan>(0, text.length, URLSpan::class.java)

        for (span in spans) {
            val start = text.getSpanStart(span)
            val end = text.getSpanEnd(span)
            text.removeSpan(span)
            val span = URLSpanNoUnderline(span.url)
            text.setSpan(span, start, end, 0)
        }
    }

    /**
     * @return пустая строка?
     */
    fun isEmpty(source: CharSequence): Boolean {
        return TextUtils.isEmpty(source)
    }

    /**
     * @return пустая строка?
     */
    fun isTrimEmpty(source: String?): Boolean {
        return source == null || source.trim { it <= ' ' }.length == 0
    }

    /**
     * @return НЕ пустая строка?
     */
    fun isNotEmpty(source: CharSequence): Boolean {
        return !isEmpty(source)
    }

    /**
     * Удалить нули в начале строки
     */
    fun removeLeadingZeros(source: String): String {
        return source.replaceFirst("^0+(?!$)".toRegex(), "")
    }

    /**
     * @return возвращает пустую строку, если исходная null
     */
    fun emptyIfNull(s: String): String {
        return if (isEmpty(s)) "" else s
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
     * Обрезать строку, если она больше определенного значения
     *
     * @param source    исходная строка
     * @param maxLength максимальная длинна строки
     * @return строка не превышающая максимального размера
     */
    fun cut(source: String, maxLength: Int): String? {
        if (TextUtils.isEmpty(source)) {
            return source
        }
        return if (source.length > maxLength)
            source.substring(0, maxLength)
        else
            source
    }

    private fun getDecimalFormat(pattern: String): DecimalFormat {
        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.groupingSeparator = ' '
        symbols.decimalSeparator = ','
        return DecimalFormat(pattern, symbols)
    }
}