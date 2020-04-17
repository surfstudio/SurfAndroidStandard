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
package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * [CompositionEvent]'s decomposition to another middleware.
 *
 * It works as follows:
 *  1) Extracts input events from [CompositionEvent].
 *  2) Passes events to another [RxMiddleware]<[T]> responsible for mapping from input to output.
 *  3) Packs output events in the [CompositionEvent] and pass it to the parent middleware.
 */
class CompositionTransformer<T: Event, C: CompositionEvent<T>>(
        private val middleware: RxMiddleware<T>
): ObservableTransformer<C, C> {

    override fun apply(upstream: Observable<C>): ObservableSource<C> {
        return upstream.flatMap { composition ->
            val inEvents = Observable.fromIterable(composition.events)
            val outEvents = middleware.transform(inEvents)
            outEvents.map { composition.apply { events = listOf(it) } }
        }
    }
}