/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Шина сообшений работающая на Rx.
 */
class RxBus {

    private val internalBus = PublishSubject.create<Any>()

    fun <T> observeEvents(eventClass: Class<T>): Observable<T> {
        return internalBus.filter { eventClass.isInstance(it) }
                .map { eventClass.cast(it) }
    }

    fun <T : Any> emitEvent(event: T) {
        internalBus.onNext(event)
    }
}
