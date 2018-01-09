package ru.surfstudio.standard.ui.util;


import android.support.annotation.StyleRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Вспомогательные методы работы со строками
 */
public class StringUtil {

    private static final String DECIMAL_VALUE_FORMAT = "%.2f";
    @SuppressWarnings("squid:S00115")
    private static final String wholeFormat = "#,###";
    @SuppressWarnings("squid:S00115")
    private static final String fractionalFormat = "###,###.##";
    private static final double VALID_VALUE = 0.00000001f;

    private static CharSequence[] symbols = new CharSequence[]{"&", "<", ">", "...", "™"};
    private static CharSequence[] tags = new CharSequence[]{"&amp;", "&lt;", "&gt;", "&hellip;", "&trade;"};

    private StringUtil() {
    }

    public static String htmlSimpleDecode(String htmlText) {
        String decoded = htmlText;
        for (int tagIndex = 0; tagIndex < tags.length; tagIndex++) {
            decoded = decoded.replace(tags[tagIndex], symbols[tagIndex]);
        }
        return decoded;
    }

    /**
     * Убирает подчеркивание у текста
     */
    public static void removeUnderlines(Spannable text) {
        URLSpan[] spans = text.getSpans(0, text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = text.getSpanStart(span);
            int end = text.getSpanEnd(span);
            text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            text.setSpan(span, start, end, 0);
        }
    }

    /**
     * @return пустая строка?
     */
    public static boolean isEmpty(CharSequence source) {
        return TextUtils.isEmpty(source);
    }

    /**
     * @return пустая строка?
     */
    public static boolean isTrimEmpty(String source) {
        return source == null || source.trim().length() == 0;
    }

    /**
     * @return НЕ пустая строка?
     */
    public static boolean isNotEmpty(CharSequence source) {
        return !isEmpty(source);
    }

    /**
     * Удалить нули в начале строки
     */
    public static String removeLeadingZeros(String source) {
        return source.replaceFirst("^0+(?!$)", "");
    }

    /**
     * @return возвращает пустую строку, если исходная null
     */
    public static String emptyIfNull(String s) {
        return isEmpty(s) ? "" : s;
    }

    /**
     * Устанавливает строку и подстроку
     */
    public static void setTextWithSubtitle(TextView textView, String text, String subtitleText, @StyleRes int subtitleStyleId) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.append("\n");
        builder.append(subtitleText);
        builder.setSpan(new TextAppearanceSpan(textView.getContext(), subtitleStyleId),
                text.length(), text.length() + subtitleText.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static String getSplitedNumber(double value) {
        //Locale.US - чтобы на выходе была строка вида *.**, а не *,**
        if (value < VALID_VALUE) {
            return String.valueOf(0);
        }

        String valueInString = String.format(Locale.US, DECIMAL_VALUE_FORMAT, value);
        Double d = Double.valueOf(valueInString);
        return getDecimalFormat(fractionalFormat).format(d);
    }

    public static String getSplitedNumber(int value) {
        return value > 0 ? getDecimalFormat(wholeFormat).format(value) : String.valueOf(0);
    }

    /**
     * Обрезать строку, если она больше определенного значения
     *
     * @param source    исходная строка
     * @param maxLength максимальная длинна строки
     * @return строка не превышающая максимального размера
     */
    public static String cut(String source, int maxLength) {
        if (TextUtils.isEmpty(source)) {
            return source;
        }
        return source.length() > maxLength
                ? source.substring(0, maxLength)
                : source;
    }

    private static DecimalFormat getDecimalFormat(String pattern) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');
        return new DecimalFormat(pattern, symbols);
    }
}