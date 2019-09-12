/*
  Copyright (c) 2018-present, SurfStudio LLC, Fedor Atyakshin.

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
package ru.surfstudio.android.shared.pref.intid


import java.lang.IllegalStateException
import java.util.*

/**
 * Позволяет из коллекции с элементами, имплементирующими IntId, получить
 * элемент по совпадающему значению
 * Изначально создано для поиска значения Enum
 */

object IntIdUtil {

    fun <T : IntId> getByValue(values: Array<T>, value: Int): T {
        values
                .filter { it.id() == value }
                .forEach { return it }
        throw IllegalStateException(String.format("Not found id: %s in values: %s with ids %s",
                value, Arrays.toString(values), ids(values)))
    }

    private fun <T : IntId> ids(values: Array<T>): List<Int> {
        val result = ArrayList<Int>(values.size)
        values.mapTo(result) { it.id() }
        return result
    }
}
