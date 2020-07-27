/*
  Copyright (c) 2020, SurfStudio LLC.

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
package ru.surfstudio.android.core.mvi.event.hub

import ru.surfstudio.android.core.mvi.event.Event

/**
 * Базовый типизированный EventHub.
 * Служит для получения и рассылки сообщений, является своеобразной шиной между вью и данными.
 *
 * @param T         тип события, которым типизирован Hub
 * @param EventStream    тип потока событий, которые испускает Hub
 */
interface EventHub<T : Event, EventStream> {

    /**
     * Отправка события
     */
    fun emit(event: T)

    /**
     * Поток отправляемых событий
     */
    fun observe(): EventStream
}