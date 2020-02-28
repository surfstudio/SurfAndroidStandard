/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.binding

/**
 * Класс обеспечивающий подписку на изменение данных.
 * Реализует концепцию источников данных: "События изменения данных принимают все, кроме эммитящего источника"
 * Это исключает прослушивания источником своих же событий изменения данных и зацикливания.
 */
@Deprecated("Используйте пакет ru.surfstudio.android.core.mvp.binding.rx")
open class BindData<T>(value: T) {

    var value: T = value
        private set

    private val listeners: MutableMap<Any, MutableSet<((T) -> Unit)>> = mutableMapOf()

    /**
     * Подписка на изменеие значения
     * @param source источник получения данных
     */
    fun observe(source: Any, listener: (T) -> Unit) {
        listeners[source]?.add(listener)
        if (listeners[source] == null) listeners[source] = mutableSetOf(listener)
    }

    /**
     * Подписывает на изменение значения и вызывает подписку для с текущим значением
     */
    fun observeAndApply(source: Any, listener: (T) -> Unit) {
        observe(source, listener)
        listener.invoke(value)
    }

    /**
     * Устанавливает значение и оповещает всех подписчиков, кроме указанного источника
     * @param source источник изменения значения
     * @param newValue новое значение переменной
     * @param eagerNotify оповещать подписчиков, даже если значение не изменилось
     */
    fun setValue(source: Any, newValue: T, eagerNotify: Boolean = false) {
        val previousValue = value
        value = newValue
        if (value == previousValue && !eagerNotify) return

        listeners.filterKeys { it != source }
                .values
                .forEach { it.forEach { it.invoke(value) } }
    }

    /**
     * Отписывает источник от всех событий
     */
    fun unObserveSource(source: Any) {
        listeners.remove(source)
    }
}

