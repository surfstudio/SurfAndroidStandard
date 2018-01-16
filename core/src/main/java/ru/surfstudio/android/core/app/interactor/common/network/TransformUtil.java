package ru.surfstudio.android.core.app.interactor.common.network;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import ru.surfstudio.android.core.util.CollectionUtils;
import ru.surfstudio.android.core.util.Mapper;
import ru.surfstudio.android.core.util.Transformable;


/**
 * содержит методы для трансформации объектов Domain слоя в данные для сервера и наоборот
 */
public final class TransformUtil {
    public static final String COLOR_HEX_FORMAT = "#%06X";
    public static final int TRANSPARENT_COLOR = 0;

    /**
     * формат даты и времени ISO8601 https://en.wikipedia.org/wiki/ISO_8601
     */
    public static final String DATE_PATTERN_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String DATE_FORMAT_FULL = "dd.MM.yyyy";
    public static final String DATE_FORMAT_TEXT_FULL = "d MMMM yyyy";
    public static final String DATE_FORMAT_WITH_TIME_FULL = "d MMMM yyyy HH:mm:ss";
    private static final String DATE_DEFAULT_FORMAT = DATE_FORMAT_FULL;

    /**
     * список стандартных форматов поддерживаемых для парсинга
     */
    private static final String[] DATE_DEFAULT_PATTERNS = new String[]{
            DATE_PATTERN_ISO8601
    };

    private TransformUtil() {
        throw new IllegalStateException("no instance allowed");
    }

    public static <T, E extends Transformable<T>> T transform(@Nullable E object) {
        return object != null ? object.transform() : null;
    }

    public static <T, E extends Transformable<T>> List<T> transformCollection(Collection<E> src) {
        return CollectionUtils.mapEmptyIfNull(src, Transformable::transform);
    }

    public static <T, E> List<T> transformCollection(Collection<E> src, final Mapper<E, T> mapper) {
        return CollectionUtils.mapEmptyIfNull(src, mapper);
    }

    public static String convertColor(int color) {
        return String.format(COLOR_HEX_FORMAT, 0xFFFFFF & color);
    }

    public static int convertColor(String color) {
        return Color.parseColor(color);
    }

    public static int convertColorTransparentIfEmpty(String color) {
        return TextUtils.isEmpty(color) ? TRANSPARENT_COLOR : convertColor(color);
    }

    /**
     * @return сконвертированное время с форматом для сервера, -1 если ошибка
     */
    public static long convertServerDate(@Nullable String dateValue) {
        if (dateValue == null) {
            return -1;
        }

        return convertDateTime(dateValue, DATE_PATTERN_ISO8601);
    }

    public static long convertDateTime(@NonNull final String dateValue) {
        return convertDateTime(dateValue, (String[]) null);
    }

    public static long convertDateTime(@NonNull final String dateValue, @NonNull final String dateFormat) {
        return convertDateTime(dateValue, new String[]{dateFormat});
    }

    public static long convertDateTime(@NonNull final String dateValue, @Nullable final String[] dateFormats) {
        Date date = parseDate(dateValue, dateFormats);
        return date != null ? date.getTime() : -1;
    }

    public static String formatDate(long date) {
        return formatDate(new Date(date));
    }

    public static String formatDate(@NonNull final String dateFormat) {
        return formatDate(getCurrentDate(), dateFormat);
    }

    public static String formatDate(@NonNull Date date) {
        return formatDate(date, DATE_DEFAULT_FORMAT);
    }

    public static String formatDate(long date, @NonNull final String dateFormat) {
        return formatDate(new Date(date), dateFormat);
    }

    public static String formatDate(@NonNull Date date, @NonNull final String dateFormat) {
        return DateFormatHolder.formatFor(dateFormat).format(date);
    }

    @Nullable
    public static Date parseDate(@NonNull final String dateValue) {
        return parseDate(dateValue, (String[]) null);
    }

    @Nullable
    public static Date parseDate(@NonNull final String dateValue, @NonNull final String dateFormat) {
        return parseDate(dateValue, new String[]{dateFormat});
    }

    // endregion

    @Nullable
    public static Date parseDate(@NonNull final String dateValue, @Nullable final String[] dateFormats) {
        final String[] localDateFormats = dateFormats != null ? dateFormats : DATE_DEFAULT_PATTERNS;
        String v = dateValue;
        // trim single quotes around date if present
        // see issue #5279
        if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
            v = v.substring(1, v.length() - 1);
        }

        for (final String dateFormat : localDateFormats) {
            final SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
            final ParsePosition pos = new ParsePosition(0);
            final Date result = dateParser.parse(v, pos);
            if (pos.getIndex() != 0) {
                return result;
            }
        }

        return null;
    }

    /**
     * Прибавляет секудны,минуты,часы и.д.т к текущей дате.
     *
     * @param calendarField необходимо брать из {@link Calendar}. например {@link Calendar#DAY_OF_MONTH}
     */
    public static Date addToCurrentDate(int calendarField, int amount) {
        return add(getCurrentDate(), calendarField, amount);
    }

    // ---------- скопироваино из Apache commons DateUtils.java --------------- //

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

    private static Date getCurrentDate() {
        return new Date(getCurrentTimeInMills());
    }

    private static long getCurrentTimeInMills() {
        Calendar c = Calendar.getInstance();
        int utcOffset = c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET);
        return c.getTimeInMillis() + utcOffset;
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

        /**
         * creates a {@link SimpleDateFormat} for the requested format string.
         *
         * @param pattern a non-<code>null</code> format String according to
         *                {@link SimpleDateFormat}. The format is not checked against
         *                <code>null</code> since all paths go through
         *                {@link TransformUtil}.
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
