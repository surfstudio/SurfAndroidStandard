package ru.surfstudio.android.core.util;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {

    private DateUtil() {
        throw new IllegalStateException(DateUtil.class.getSimpleName()
                + " синглтон");
    }

    public static SimpleDateFormat formatFor(final String pattern) {
        return DateFormatHolder.formatFor(pattern);
    }

    // ---------- скопироваино из Apache commons DateUtils.java --------------- //

    /**
     * Прибавляет секудны,минуты,часы и.д.т к текущей дате.
     *
     * @param calendarField необходимо брать из {@link Calendar}. например {@link Calendar#DAY_OF_MONTH}
     */
    public static Date addToCurrentDate(int calendarField, int amount) {
        return add(getCurrentDate(), calendarField, amount);
    }

    /**
     * Adds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date          Дата
     * @param calendarField Поле для прибавления (напр. {@link Calendar#DAY_OF_MONTH})
     * @param amount        количество
     * @return новая дата
     */
    public static Date add(@NotNull Date date, int calendarField, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * Checks if two date objects are on the same day ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     */
    public static boolean isSameDay(@NonNull Date date1, @NonNull Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

    }

    private static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * A factory for {@link SimpleDateFormat}s. The instances are stored in a
     * threadlocal way because SimpleDateFormat is not threadsafe as noted in
     * {@link SimpleDateFormat its javadoc}.
     */
    @SuppressWarnings("squid:ModifiersOrderCheck")
    private final static class DateFormatHolder {
        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS =
                new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {
                    @Override
                    protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
                        return new SoftReference<>(new HashMap<String, SimpleDateFormat>());
                    }
                };

        private DateFormatHolder() {
        }

        /**
         * creates a {@link SimpleDateFormat} for the requested format string.
         *
         * @param pattern a non-<code>null</code> format String according to
         *                {@link SimpleDateFormat}. The format is not checked against
         *                <code>null</code> since all paths go through.
         * @return the requested format. This simple dateformat should not be used
         * to {@link SimpleDateFormat#applyPattern(String) apply} to a
         * different pattern.
         */
        static SimpleDateFormat formatFor(final String pattern) {
            final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref.get();
            if (formats == null) {
                formats = new HashMap<>();
                THREADLOCAL_FORMATS.set(new SoftReference<>(formats));
            }

            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern, Locale.getDefault());
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                formats.put(pattern, format);
            }

            return format;
        }
    }
}
