package ru.surfstudio.android.core.util;

import android.graphics.Color;
import android.text.TextUtils;

/**
 * Утилитарный класс для работы с цветами
 */

public class ColorUtil {
    public static final String COLOR_HEX_FORMAT = "#%06X";
    public static final int TRANSPARENT_COLOR = 0;

    private ColorUtil() {
        //non instantiate class
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
}
