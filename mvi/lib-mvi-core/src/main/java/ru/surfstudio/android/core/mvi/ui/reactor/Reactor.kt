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
package ru.surfstudio.android.core.mvi.ui.reactor

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter

/**
 * Класс, реагирующий на событие изменением текущего состояния View.
 *
 * Чтобы не возвращаться к хранению всего состояния экрана в одном классе, мы не создаем каждый раз новое состояние,
 * а изменяем его, и даем View отреагировать на это с помощью Rx.
 *
 * [E] - тип события, на которое реагирует реактор
 * [H] - тип StateHolder (хранителя состояния)
 */
interface Reactor<E : Event, H> : StateEmitter {

    /**
     * Реакция на событие
     *
     * @param sh StateHolder, который содержит логику смены состояния
     * @param event событие, на которое реагирует reactor
     */
    fun react(sh: H, event: E)
}