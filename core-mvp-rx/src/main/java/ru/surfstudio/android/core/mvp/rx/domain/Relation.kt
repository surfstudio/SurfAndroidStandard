/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface Relation<V, in S : RelationEntity, in T : RelationEntity> {

    val value: V

    val hasValue: Boolean

    fun getConsumer(source: S): Consumer<V>

    fun getObservable(target: T): Observable<V>
}

interface Related<S : RelationEntity> {

    fun relationEntity(): S

    fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>): Disposable

    fun <T> Relation<T, *, S>.getObservable() =
            this.getObservable(relationEntity())

    fun <T> Relation<T, S, *>.getConsumer() =
            this.getConsumer(relationEntity())

    fun <T> Relation<T, S, *>.accept(newValue: T) =
            this.getConsumer().accept(newValue)

    infix fun <T> Observable<T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this, consumer)

    infix fun <T> Single<T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this.toObservable(), consumer)

    fun <T, R> Observable<T>.bindTo(consumer: Consumer<R>, transformer: (T) -> R) =
            this@Related.subscribe(this.map { transformer(it) }, consumer)

    infix fun <T> Observable<T>.bindTo(consumer: (T) -> Unit) =
            this@Related.subscribe(this, Consumer { consumer(it) })

    infix fun <T> Relation<T, *, S>.bindTo(consumer: (T) -> Unit) =
            this.getObservable()
                    .bindTo(consumer)

    infix fun <T> Observable<T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.getConsumer())

    infix fun <T> Single<T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.getConsumer())

    infix fun <T> Relation<T, *, S>.bindTo(relation: Relation<T, S, *>) =
            this.getObservable().bindTo(relation.getConsumer())

    fun <T, R> Relation<T, *, S>.bindTo(relation: Relation<R, S, *>, transformer: (T) -> R) =
            this.getObservable().bindTo(relation.getConsumer(), transformer)
}

interface RelationEntity

interface ActionSource : RelationEntity
interface ActionTarget : RelationEntity

interface CommandSource : RelationEntity
interface CommandTarget : RelationEntity

interface StateSource : RelationEntity
interface StateTarget : RelationEntity

object VIEW : ActionSource, CommandTarget, StateSource, StateTarget
object PRESENTER : ActionTarget, CommandSource, StateSource, StateTarget
