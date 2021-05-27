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
package ru.surfstudio.android.core.mvi.ui

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindableRxView

/**
 * Базовая реактивная вью, которая может отправлять события в RxEventHub.
 */
interface BaseReactView : BindableRxView {

    /**
     * Отправка события каждый раз, когда [Observable] эмитит новое значение.
     *
     * @param hub хаб, в который отправляется событие
     */
    fun <T : Event> Observable<out T>.emit(hub: RxEventHub<T>) =
            this as Observable<T> bindTo hub

    /**
     * Отправка события каждый раз, когда [Observable] эмитит новое значение.
     *
     * @param event     отправляемое событие
     * @param hub       хаб, в который отправляется событие
     */
    fun <T, E : Event> Observable<T>.emit(event: E, hub: RxEventHub<E>) =
            this.map { event } bindTo hub

    /**
     * Отправка события.
     *
     * @param hub хаб, в который отправляется событие
     */
    fun <T : Event> T.emit(hub: RxEventHub<T>) = hub.emit(this)
}