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
import io.reactivex.ObservableTransformer

/**
 * Transformer which is responsible of mapping stream:
 * It takes stream of [Observable]<[T]> and transforms it to [Observable]<[E]>
 */
class StreamMapTransformer<T, E>(
        private val mapper: (Observable<T>) -> Observable<out E>
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): Observable<E> {
        return mapper(upstream) as Observable<E>
    }
}
