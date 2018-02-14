package ru.surfstudio.android.kotlinextensions.text

/**
 * Расширения для строк
 */

/**
 * Обрезать строку, если она больше определенного значения
 *
 * @param source    исходная строка
 * @param maxLength максимальная длинна строки
 * @return строка не превышающая максимального размера
 */
fun CharSequence.cut(maxLength: Int): String = take(maxLength).toString()

/**
 * @return возвращает пустую строку, если исходная null
 */
fun String?.emptyIfNull(): String = this ?: ""

/**
 * Умножает строку на число, может записываться в инфиксной форме string * n
 * или .times(n)
 */
operator fun String.times(n: Int): String {
    var s: String = this
    for (i in 2..n) s += this
    return s
}