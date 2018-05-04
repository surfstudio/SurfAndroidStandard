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
package ru.surfstudio.android.utilktx.ktx.text

/**
 * Файл с символьными константами
 */
val SPACE: String = " "
val NEXT_LINE: String = "\n"
val EMPTY_STRING: String = ""
val HYPHEN_STRING: String = "-"
val APOSTROPHE_STRING: String = "'"
val PHONE_NUMBER_CHARS = charArrayOf('+', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
val DECIMAL_VALUE_FORMAT = "%.2f"
val wholeFormat = "#,###"
val fractionalFormat = "###,###.##"
val VALID_VALUE = 0.00000001

val symbols = arrayOf<CharSequence>("&", "<", ">", "...", "™")
val tags = arrayOf<CharSequence>("&amp;", "&lt;", "&gt;", "&hellip;", "&trade;")