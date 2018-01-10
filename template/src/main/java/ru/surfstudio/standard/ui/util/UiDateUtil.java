package ru.surfstudio.standard.ui.util;


import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UiDateUtil {

    private static final String ROLL_DATE_PATTERN = "HH:mm";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(ROLL_DATE_PATTERN, Locale.getDefault());

    private UiDateUtil() {
    }

    /**
     * @return дата в формате "15:00"
     */
    public static String formatDateForDelivery(@NonNull Date date) {
        return sdf.format(date);
    }

    public static String formatDateForDeliveryEmptyIfNull(Date date) {
        return date != null ? formatDateForDelivery(date) : "";
    }
}
