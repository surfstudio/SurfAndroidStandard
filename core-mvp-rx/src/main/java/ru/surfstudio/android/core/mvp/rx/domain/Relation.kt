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

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface Relation<V, in S : RelationEntity, in T : RelationEntity> {

    fun getConsumer(source: S): Consumer<V>

    fun getObservable(target: T): Observable<V>
}

interface Related<S : RelationEntity> {

    fun relationEntity(): S

    fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>): Disposable


    val <T> Relation<T, *, S>.observable: Observable<T>
        get() =
            this.getObservable(relationEntity())

    val <T> Relation<T, S, *>.consumer: Consumer<T>
        get() =
            this.getConsumer(relationEntity())


    val <T> ValuableRelation<T, S, *>.value: T
        get() =
            this.internalValue


    fun <T> Relation<T, S, *>.accept(newValue: T) =
            this.consumer.accept(newValue)

    fun Relation<Unit, S, *>.accept() =
            this.consumer.accept(Unit)


    infix fun <T> Observable<T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this, consumer)

    infix fun <T> Observable<T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.consumer)

    infix fun <T> Single<T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this.toObservable(), consumer)

    infix fun <T> Single<T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.consumer)

    infix fun <T> Maybe<T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this.toObservable(), consumer)

    infix fun <T> Maybe<T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.consumer)

    infix fun Completable.bindTo(consumer: Consumer<Unit>) =
            this@Related.subscribe(this.toObservable(), consumer)

    infix fun Completable.bindTo(relation: Relation<Unit, S, *>) =
            this.bindTo(relation.consumer)


    infix fun <T> Observable<T>.bindTo(consumer: (T) -> Unit) =
            this@Related.subscribe(this, Consumer { consumer(it) })

    infix fun Observable<Unit>.bindTo(consumer: () -> Unit) =
            this@Related.subscribe(this, Consumer { consumer() })


    infix fun <T> Relation<T, *, S>.bindTo(consumer: (T) -> Unit) =
            this.observable
                    .bindTo(consumer)

    infix fun Relation<Unit, *, S>.bindTo(consumer: () -> Unit) =
            this.observable
                    .bindTo(consumer)

    infix fun <T> Relation<T, *, S>.bindTo(relation: Relation<T, S, *>) =
            this.observable.bindTo(relation.consumer)
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
