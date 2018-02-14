package ru.surfstudio.android.ktx.extensions.text

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