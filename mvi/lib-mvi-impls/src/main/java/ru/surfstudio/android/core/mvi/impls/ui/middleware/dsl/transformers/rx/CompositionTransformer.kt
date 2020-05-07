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
import java.lang.IllegalStateException

/**
 * [CompositionEvent]'s decomposition to another middleware.
 *
 * It works as follows:
 *  1) Extracts input events from [CompositionEvent].
 *  2) Passes events to another [RxMiddleware]<[T]> responsible for mapping from input to output.
 *  3) Packs output events in the [CompositionEvent] and pass it to the parent middleware.
 *
 *  If there's no input events, it can still produce output events if necessary.
 *
 *  Output events are created by calling no-args constructor,
 *  so composition event [C] must have one.
 */
class CompositionTransformer<T : Event, C : CompositionEvent<T>>(
        private val compositionEventClass: Class<C>,
        private val middleware: RxMiddleware<T>
) : ObservableTransformer<C, C> {

    override fun apply(upstream: Observable<C>): ObservableSource<C> {
        val inEvents = upstream.flatMap { composition -> Observable.fromIterable(composition.events) }
        val outEvents = middleware.transform(inEvents)
        return outEvents.map(::createCompositionEvent)
    }

    private fun createCompositionEvent(nestedEvent: T): C {
        val newCompositionEvent = try {
            compositionEventClass.newInstance()
        } catch (exception: InstantiationException) {
            throw CompositionEventNoConstructorException()
        }

        return newCompositionEvent.apply { events = listOf(nestedEvent) }
    }

    companion object {

        /**
         * Creates [CompositionTransformer].
         *
         * We use static method instead of constructor
         * to enable type erasure avoiding mechanism by Kotlin.
         */
        inline fun <T : Event, reified C : CompositionEvent<T>> create(
                middleware: RxMiddleware<T>
        ): CompositionTransformer<T, C> {
            val transformationClass = C::class.java
            return CompositionTransformer(transformationClass, middleware)
        }
    }

    class CompositionEventNoConstructorException :
            IllegalStateException("All composition events must have no args constructor!")
}