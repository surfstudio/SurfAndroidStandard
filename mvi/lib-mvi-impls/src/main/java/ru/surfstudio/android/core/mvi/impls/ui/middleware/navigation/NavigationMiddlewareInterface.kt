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
package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close.*
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.OpenScreenForResult
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.open.OpenScreenEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute
import java.io.Serializable

/**
 * Интерфейс для middleware с поддержкой навигации.
 *
 * Обрабатывает события [OpenScreenEvent] и [CloseScreenEvent], определяя нужные навигаторы по классам
 *
 * Является экспериментальной фичей и не рекомендуется для использования на стабильных проектах.
 */
interface NavigationMiddlewareInterface<T : Event> : RxMiddleware<T> {

    var screenNavigator: ScreenNavigator

    /**
     * Opens the screen when [OpenScreenEvent] appears on eventStream.
     */
    fun openScreenByEvent(event: OpenScreenEvent) {
        if (event is OpenScreenForResult<*>) {
            screenNavigator.openForResult(event.route)
        } else {
            screenNavigator.open(event.route)
        }
    }

    /**
     * Closes the screen when [CloseScreenEvent] appears on eventStream.
     */
    fun closeScreenByEvent(event: CloseScreenEvent) {
        when (event) {
            is CloseActivityEvent ->
                screenNavigator.close()

            is CloseTaskEvent ->
                screenNavigator.closeTask()

            is CloseWithResultEvent<*> -> {
                val resultRoute = event.route as SupportOnActivityResultRoute<Serializable>
                screenNavigator.closeWithResult(resultRoute, event.result, event.isSuccess)
            }

            is CloseFragmentEvent -> {
                screenNavigator.close(event.route)
            }

            is CloseDialogEvent -> {
                screenNavigator.close(event.route)
            }
        }
    }

    /**
     * Automatically opens/closes screen when [CloseScreenEvent] or [OpenScreenEvent]
     * appears in eventStream.
     */
    fun Observable<T>.mapNavigationAuto() =
            flatMap { event ->
                when (event) {
                    is OpenScreenEvent -> openScreenByEvent(event)
                    is CloseScreenEvent -> closeScreenByEvent(event)
                }
                Observable.empty<T>()
            }
}