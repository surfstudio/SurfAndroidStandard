package ru.surfstudio.android.utilktx.ktx.text

/**
 * Расширения для строк
 */

/**
 * Обрезать строку, если она больше определенного значения
 *
 * @param maxLength максимальная длинна строки
 * @return строка не превышающая максимального размера
 */
fun CharSequence.cut(maxLength: Int): String = take(maxLength).toString()

/**
 * @return возвращает пустую строку, если исходная null
 */
fun String?.emptyIfNull(): String = this ?: EMPTY_STRING

/**
 * Умножает строку на число, может записываться в инфиксной форме string * n
 * или .times(n)
 */
operator fun String.times(n: Int): String {
    var s: String = this
    for (i in 2..n) s += this
    return s
}