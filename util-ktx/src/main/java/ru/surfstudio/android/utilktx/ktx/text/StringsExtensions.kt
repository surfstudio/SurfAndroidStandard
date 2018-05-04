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