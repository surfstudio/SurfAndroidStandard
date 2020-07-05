package ru.surfstudio.standard.base.util

// TODO: обновить по нуждам приложения

/**
 * Паттерны для форматирования даты
 */
object DatePattern {
    const val DAY_MONTH_AND_WEEK_PATTERN = "d MMMM, E"
    const val DAY_MONTH_PATTERN = "dd MMMM"
    const val DAY_MONTH_SHORT_PATTERN = "dd MMM"
    const val SHORT_DAY_SHORT_MONTH_PATTERN = "d MMM"
    const val DAY_MONTH_AND_TIME_PATTERN = "d MMMM, HH:mm"
    const val DAY_MONTH_YEAR_AND_TIME_PATTERN = "dd.MM.yyyy, HH:mm"
    const val MONTH_PATTERN = "LLLL"
    const val MONTH_YEAR_PATTERN = "LLLL yyyy"
    const val MONTH_SHORT_PATTERN = "MM"
    const val MONTH_YEAR_SHORT_PATTERN = "MM/yy"
    const val MONTH_DAY_PATTERN = "MM/dd"
    const val YEAR_MONTH_PATTERN = "yy/MM"
    const val YEAR_PATTERN = "yyyy"
    const val HUMAN_READABLE_PATTERN = "d MMMM yyyy"
    const val HUMAN_READABLE_WITH_WEEK_DAY_PATTERN = "d MMMM, EEEE"
    const val ROLL_DATE_PATTERN = "HH:mm"
    const val FORM_DATE_PATTERN = "yyyy-MM-dd"
    const val DATE_DOTTED_PATTERN = "dd.MM.yyyy"
    const val DATE_DOTTED_SHORTEN_YEAR_PATTERN = "dd.MM.yy"
    const val DATE_SLASHED_PATTERN = "dd/MM/yyyy"
    const val MONTH_YEAR_SLASHED_PATTERN = "MM/yyyy"
    const val DATE_PATTERN_WITH_TIME = "HH:mm dd.MM.yyyy"
    const val DATE_ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ"
    const val DATE_TIME_FORMAT_SHORT = "yyyy-MM-dd'T'HH:mm:ss"
}
