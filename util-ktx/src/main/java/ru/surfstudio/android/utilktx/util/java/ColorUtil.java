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
