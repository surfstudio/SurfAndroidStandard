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
package ru.surfstudio.android.utilktx.util.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@SuppressWarnings("WeakerAccess")
public class DateUtil {

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

    private DateUtil() {
        throw new IllegalStateException(DateUtil.class.getSimpleName()
                + " синглтон");
    }

    public static SimpleDateFormat formatFor(final String pattern) {
        return DateFormatHolder.formatFor(pattern);
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

    public static String formatDate(long date, int gmtOffsetInMillis) {
        return formatDate(new Date(date), gmtOffsetInMillis);
    }

    public static String formatDate(@NonNull final String dateFormat) {
        return formatDate(getCurrentDate(), dateFormat);
    }

    public static String formatDate(@NonNull final String dateFormat, int gmtOffsetInMillis) {
        return formatDate(getCurrentDate(), dateFormat, gmtOffsetInMillis);
    }

    public static String formatDate(@NonNull Date date) {
        return formatDate(date, DATE_DEFAULT_FORMAT);
    }

    public static String formatDate(@NonNull Date date, int gmtOffsetInMillis) {
        return formatDate(date, DATE_DEFAULT_FORMAT, gmtOffsetInMillis);
    }

    public static String formatDate(long date, @NonNull final String dateFormat) {
        return formatDate(new Date(date), dateFormat);
    }

    public static String formatDate(long date, @NonNull final String dateFormat, int gmtOffsetInMillis) {
        return formatDate(new Date(date), dateFormat, gmtOffsetInMillis);
    }

    public static String formatDate(@NonNull Date date, @NonNull final String dateFormat) {
        return DateFormatHolder.formatFor(dateFormat).format(date);
    }

    public static String formatDate(@NonNull Date date, @NonNull final String dateFormat, int gmtOffsetInMillis) {
        return DateFormatHolder.formatFor(dateFormat, gmtOffsetInMillis).format(date);
    }

    public static int getDeviceGmtOffsetInMillis() {
        TimeZone defaultTimeZone = TimeZone.getDefault();
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        return defaultTimeZone.getOffset(currentTimeInMillis);
    }

    /**
     * Parse {@link java.util.Date} from {@link java.lang.String} representation using {@link DateUtil#DATE_DEFAULT_FORMAT}
     * @param dateValue string representation of date
     * @return java.util.Date instance or NULL in case of parsing failure
     */
    @Nullable
    public static Date parseDate(@Nullable final String dateValue) {
        return parseDate(dateValue, (String[]) null);
    }

    /**
     * Parse {@link java.util.Date} from {@link java.lang.String} representation using provided date format
     * @param dateValue string representation of date
     * @param dateFormat format used to format date
     * @return {@link java.util.Date} instance or NULL in case of parsing failure
     */
    @Nullable
    public static Date parseDate(@Nullable final String dateValue, @NonNull final String dateFormat) {
        return parseDate(dateValue, new String[]{dateFormat});
    }

    // endregion

    /**
     * Parse {@link java.util.Date} from {@link java.lang.String} representation using provided date formats
     * @param dateValue string representation of date
     * @param dateFormats format list, one of which was used for date formatting
     * @return {@link java.util.Date} instance or NULL in case of parsing failure
     */
    @Nullable
    public static Date parseDate(@Nullable final String dateValue, @Nullable final String[] dateFormats) {
        if (dateValue != null) {
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
        } else {
            return null;
        }
    }

    // ---------- скопироваино из Apache commons DateUtils.java --------------- //

    /**
     * Прибавляет секудны, минуты, часы и т.д. к текущей дате.
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

        private static final int ZERO_GMT_OFFSET = 0;
        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS =
                new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {
                    @Override
                    protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
                        return new SoftReference<>(new HashMap<String, SimpleDateFormat>());
                    }
                };

        private DateFormatHolder() {
        }

        static SimpleDateFormat formatFor(final String pattern) {
            return formatFor(pattern, ZERO_GMT_OFFSET);
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
        static SimpleDateFormat formatFor(final String pattern, int gmtOffsetInMillis) {
            final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref.get();
            if (formats == null) {
                formats = new HashMap<>();
                THREADLOCAL_FORMATS.set(new SoftReference<>(formats));
            }

            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern, Locale.getDefault());
                formats.put(pattern, format);
            }

            if (format.getTimeZone().getRawOffset() != gmtOffsetInMillis) {
                TimeZone timeZone = TimeZone.getTimeZone("GMT");
                timeZone.setRawOffset(gmtOffsetInMillis);
                format.setTimeZone(timeZone);
            }

            return format;
        }
    }

    /**
     * Changes {@link java.lang.String} representation of date from input format to output format
     * @param inputDate original {@link java.lang.String} representation of date
     * @param inputFormat format of original {@link java.lang.String}
     * @param outputFormat desired format of {@link java.lang.String}
     * @return {@link java.lang.String} in desired format, NULL reformat attempt failed
     */
    public static String reformatDate(
            @Nullable String inputDate,
            @NonNull String inputFormat,
            @NonNull String outputFormat
    ) {
        if (inputDate == null) {
            return null;
        } else {
            Date parsed = parseDate(inputDate, inputFormat);
            if (parsed != null) {
                return new SimpleDateFormat(outputFormat, Locale.getDefault()).format(parsed);
            } else {
                return null;
            }
        }
    }
}
