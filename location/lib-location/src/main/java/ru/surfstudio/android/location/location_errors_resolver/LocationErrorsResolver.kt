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
package ru.surfstudio.android.location.location_errors_resolver

import io.reactivex.Observable
import io.reactivex.Single
import ru.surfstudio.android.location.location_errors_resolver.resolutions.LocationErrorResolution

/**
 * Утилита для решения проблем, связанных с невозможностью получения местоположения.
 */
internal object LocationErrorsResolver {

    /**
     * Решить проблемы, связанные с невозможностью получения местоположения.
     *
     * @param throwables [List], содержащий исключения связанные с невозможностью получения местоположения.
     * @param resolutions [List], содержащий решения проблем связанных с невозможностью получения местоположения.
     *
     * @return [Single]:
     * - onSuccess вызывется при завершении решения проблем. Содержит [List] из исключений, для которых не передавались
     * решения;
     * - onError вызывается в случае, если попытка решения проблем не удалась. Приходит [ResolutionFailedException].
     */
    fun resolve(throwables: List<Throwable>, resolutions: List<LocationErrorResolution<*>>): Single<List<Throwable>> =
            resolveWithMutableLists(throwables.toMutableList(), resolutions.toMutableList())

    private fun resolveWithMutableLists(
            throwables: MutableList<Throwable>,
            resolutions: MutableList<LocationErrorResolution<*>>
    ): Single<List<Throwable>> =
            observeFirstValidThrowableWithResolutionPair(throwables, resolutions)
                    .flatMap { (throwable, resolution) ->
                        resolution
                                .perform(throwable)
                                .toSingle { Pair(throwable, resolution) }
                    }
                    .flatMap { (resolvedThrowable, performedResolution) ->
                        throwables.remove(resolvedThrowable)
                        resolutions.remove(performedResolution)
                        resolveWithMutableLists(throwables, resolutions)
                    }
                    .onErrorResumeNext { t: Throwable ->
                        if (t is NoSuchElementException) {
                            Single.just(throwables)
                        } else {
                            Single.error(t)
                        }
                    }

    private fun observeFirstValidThrowableWithResolutionPair(
            throwables: List<Throwable>,
            resolutions: List<LocationErrorResolution<*>>
    ): Single<Pair<Throwable, LocationErrorResolution<*>>>  =
            observeAllPairs(throwables, resolutions)
                    .filter { (throwable, resolution) -> resolution.resolvingThrowableClass.isInstance(throwable) }
                    .firstOrError()

    private fun observeAllPairs(
            throwables: List<Throwable>,
            resolutions: List<LocationErrorResolution<*>>
    ): Observable<Pair<Throwable, LocationErrorResolution<*>>> =
            Observable
                    .fromIterable(throwables)
                    .flatMap { throwable ->
                        Observable
                                .fromIterable(resolutions)
                                .map { resolution -> Pair(throwable, resolution) }
                    }
}