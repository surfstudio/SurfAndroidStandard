/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.standard.i_network.network

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Extension функции для rx.reactiveio.Observable
 */

/**
 * Преобразует содержание Observable, которое наследует [Transformable]
 */
fun <T : Transformable<R>, R> Observable<T>.transform(): Observable<R> = map { it.transform() }

/**
 * Преобразует коллекцию внутри Observable, элементы которой являются [Transformable]
 */
fun <T : Collection<Transformable<R>>, R> Observable<T>.transformCollection(): Observable<List<R>> =
        map {
            it.map {
                it.transform()
            }
        }

/**
 * Преобразует содержание Single, которое наследует [Transformable]
 */
fun <T : Transformable<R>, R> Single<T>.transform(): Single<R> = map { it.transform() }

/**
 * Преобразует коллекцию внутри Single, элементы которой являются [Transformable]
 */
fun <T : Collection<Transformable<R>>, R> Single<T>.transformCollection(): Single<List<R>> =
        map {
            it.map {
                it.transform()
            }
        }

