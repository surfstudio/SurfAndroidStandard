/*
 * Copyright 2016 Valeriy Shtaits.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.impl.base

import io.reactivex.Completable
import io.reactivex.Single
import ru.surfstudio.android.location.deprecated.location_errors_resolver.resolutions.LocationErrorResolution

/**
 * Основа для решения проблемы получения местоположения.
 */
@Deprecated("Prefer using new implementation")
abstract class BaseLocationErrorResolutionImpl<E : Throwable> : LocationErrorResolution<E> {

    protected abstract fun performWithCastedThrowable(resolvingThrowable: E): Completable

    final override fun perform(resolvingThrowable: Throwable): Completable {
        return Single.just(resolvingThrowable)
                .map { resolvingThrowableClass.cast(resolvingThrowable) }
                .flatMap { castedResolvingThrowable ->
                    performWithCastedThrowable(castedResolvingThrowable)
                            .toSingle { castedResolvingThrowable }
                }
                .ignoreElement()
    }
}