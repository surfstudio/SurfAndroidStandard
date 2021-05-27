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
package ru.surfstudio.android.core.mvi.ui.middleware

import ru.surfstudio.android.core.mvi.event.Event

/**
 * Базовая сущность Middleware в терминологии MVI.
 * Трансформирует входящий поток событий из EventHub, и направляет его обратно.
 *
 * Эта сущность используется для трансформации событий Ui-слоя, задействуя элементы сервисного слоя.
 * Например, для осуществления загрузки данных по нажатию на кнопку.
 *
 * @param T             тип события, которым типизирован Hub
 * @param InputStream   тип входного потока событий
 * @param OutputStream  тип выходящего потока событий
 */
interface Middleware<T : Event, InputStream, OutputStream> {

    fun transform(eventStream: InputStream): OutputStream
}