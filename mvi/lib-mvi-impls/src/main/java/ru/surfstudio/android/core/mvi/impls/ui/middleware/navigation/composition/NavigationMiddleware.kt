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
package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.NavigationMiddlewareInterface
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.ScreenNavigator

/**
 * [NavigationMiddlewareInterface] implementation.
 * It works with [NavigationEvent] and adds automatic listen for result functional.
 */
class NavigationMiddleware(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        override var screenNavigator: ScreenNavigator
) : NavigationMiddlewareInterface<NavigationEvent>,
        BaseMiddleware<NavigationEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<NavigationEvent>): Observable<out NavigationEvent> =
            eventStream.mapNavigationAuto()
}