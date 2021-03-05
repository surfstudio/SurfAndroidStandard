/*
  Copyright (c) 2018-present, SurfStudio LLC, Georgiy Kartashov.

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
package ru.surfstudio.standard.base.test.matcher

import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldBeTypeOf
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.Request

/**
 * Verifies that [Event] is [RequestEvent].
 */
inline fun <reified T, reified R : Request<T>> Event?.shouldBeRequest(): R {
    return this.shouldBeInstanceOf<RequestEvent<T>>()
            .request
            .shouldBeTypeOf<R>()
}

/**
 * Verifies that [RequestEvent] is [Request.Loading].
 */
inline fun <reified T> Event?.shouldBeRequestLoading(): Request.Loading<T> {
    return this.shouldBeRequest<T, Request.Loading<T>>()
            .shouldBeTypeOf<Request.Loading<T>>()
}

/**
 * Verifies that [RequestEvent] is [Request.Success].
 */
inline fun <reified T> Event?.shouldBeRequestSuccess(): Request.Success<T> {
    return this.shouldBeRequest<T, Request.Success<T>>()
            .shouldBeTypeOf<Request.Success<T>>()
}

/**
 * Verifies that [RequestEvent] is [Request.Error].
 */
inline fun <reified E : Throwable> Event?.shouldBeRequestError(): Request.Error<E> {
    return this.shouldBeRequest<E, Request.Error<E>>()
            .shouldBeTypeOf<Request.Error<E>>()
}

/**
 * Verifies that [Request.Success] contains value of type [T].
 */
inline fun <reified T : Any> Request.Success<T>?.withValue() {
    this?.getDataOrNull().shouldBeTypeOf<T>()
}

/**
 * Verifies that [Request.Success] contains exact [value] of type [T].
 */
inline fun <reified T> Request.Success<T>?.withValue(value: T?) {
    this?.getDataOrNull().shouldBeSameInstanceAs(value)
}

/**
 * Verifies that [Request.Error] contains error of type [E].
 */
inline fun <reified E : Throwable> Request.Error<E>?.withError() {
    this?.getErrorOrNull().shouldBeTypeOf<E>()
}