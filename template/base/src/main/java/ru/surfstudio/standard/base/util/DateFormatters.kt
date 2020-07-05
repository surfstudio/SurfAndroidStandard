package ru.surfstudio.standard.base.util

import org.threeten.bp.format.DateTimeFormatter

// TODO: обновить по нуждам приложения

/**
 * Форматтеры для дат в приложении. Могут быть использованы как для парсинга даты и времени, так и для форматирования вывода
 */

private const val DAY_FULL_MONTH_PATTERN = "d MMMM"
private const val SHORT_DAY_SHORT_MONTH_PATTERN = "d MMM"
private const val HUMAN_READABLE_PATTERN = "d MMMM yyyy"
private const val MONTH_SHORT_PATTERN = "MM"
private const val YEAR_PATTERN = "yyyy"
private const val MONTH_YEAR_SHORT_PATTERN = "MM/yy"
private const val DATE_DOTTED_PATTERN = "dd.MM.yyyy"
private const val LOCAL_DATE_ISO_PATTERN = "yyyy-MM-dd"
private const val ZERO_OFFSET_DATE_TIME_ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val OFFSET_DATE_TIME_ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val OFFSET_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZ"
private const val LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
private const val LOCAL_DATE_TIME_PATTERN_WITHOUT_DIVIDER = "yyyy-MM-dd HH:mm:ss"

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату: День месяца с минимально возможным количеством цифр
 * и полное название месяца в текущей локали пользователя
 * Пример результата: 3 декабря
 */
val dayFullMonthNameFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(DAY_FULL_MONTH_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату: День месяца с минимально возможным количеством цифр
 * и короткое название месяца в текущей локали пользователя
 * Пример результата: 3 дек
 */
val dayShortMonthNameFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(SHORT_DAY_SHORT_MONTH_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату: номер месяца в году
 * Пример результата: 07
 */
val monthOfYearFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(MONTH_SHORT_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату: год
 * Пример результата: 1990
 */
val yearFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(YEAR_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату: две цифры номер месяца/две последние цифры года
 * Пример результата: 05/90
 */
val monthSlashYearFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(MONTH_YEAR_SHORT_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату: День месяца с минимально возможным количеством цифр
 * полное название месяца год
 * Пример результата: 4 января 1990
 */
val dayFullMonthNameYearFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(HUMAN_READABLE_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату ISO локальной даты: год-месяц-день через дефисы
 * Пример результата: 1990-01-04
 */
val localDateISOFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(LOCAL_DATE_ISO_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату: день.месяц.год
 * Пример результата: 04.01.1990
 */
val dateDottedFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(DATE_DOTTED_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату локальной даты времени без смещения: год-месяц-деньTчасы:минуты:секунды
 * Пример результата: 2011-12-03T10:15:30
 */
val dateTimeLocalFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату ISO для даты времени с нулевым смещением: год-месяц-деньTчасы:минуты:секунды'Z'
 * Пример результата: 2011-12-03T10:15:30Z
 */
val zeroTimeOffsetFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(ZERO_OFFSET_DATE_TIME_ISO_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату даты времени со смещением: год-месяц-деньTчасы:минуты:секунды+-смещение
 * Для этого форматтера смещение записывается формате +-часыминуты, пример -0800. (без двоеточия из-за этого это не ISO формат)
 * Пример результата: 2011-12-03T10:15:30-0800
 */
val dateTimeWithZoneOffsetFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(OFFSET_DATE_TIME_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату ISO для даты времени со смещением: год-месяц-деньTчасы:минуты:секунды+-смещение
 * Для ISO смещение записывается всегда точно в формате +-часы:минуты, пример -08:00.
 * Пример результата: 2011-12-03T10:15:30-08:00
 */
val dateTimeWithZoneOffsetISOFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(OFFSET_DATE_TIME_ISO_PATTERN) }

/**
 * Форматтер для даты/времени. Приводит строку/дату к формату локальной даты времени без смещения: год-месяц-день часы:минуты:секунды
 * Пример результата: 2011-12-03 10:15:30
 */
val dateTimeLocalWithoutDividerFormatter: DateTimeFormatter by lazy { DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN_WITHOUT_DIVIDER) }
