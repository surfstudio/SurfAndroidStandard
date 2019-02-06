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
package ru.surfstudio.android.location.location_errors_resolver.resolutions

import io.reactivex.Completable

/**
 * Решение проблемы, связанной с невозможностью получения местоположения.
 */
interface LocationErrorResolution<E : Throwable> {

    val resolvingThrowableClass: Class<E>

    /**
     * Применить решение проблемы, связанной с невозможностью получения местоположения.
     *
     * @param resolvingThrowable Исключение, возникшее во время получения местоположения, которое нужно исправить.
     *
     * @return [Completable]:
     * - onComplete() вызывается в случае успешного решения проблемы;
     * - onError вызывается в случае, если попытка решения проблемы не удалась. Передается [ResolutionFailedException].
     */
    fun perform(resolvingThrowable: Throwable): Completable
}