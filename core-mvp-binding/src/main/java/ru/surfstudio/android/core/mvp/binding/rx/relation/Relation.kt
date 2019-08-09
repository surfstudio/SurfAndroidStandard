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

package ru.surfstudio.android.core.mvp.binding.rx.relation

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Описывает тип сущности, которая учавствует в обмене данными
 * Источник или получатель данных. По этому признаку отличаается источник от получателя
 * Например — Презентер, Вью и т.п.
 */
interface RelationEntity

/**
 *  Описывает механизм переадачи данных. И определяет возможности для
 *  `S` источника данных и `T` получателя данных
 */
abstract class Relation<V, in S : RelationEntity, in T : RelationEntity> {

    internal abstract fun getConsumer(source: S): Consumer<V>

    internal abstract fun getObservable(target: T): Observable<V>
}

/**
 * [Relation] который сохраняет последнее переданное значение
 */
abstract class ValuableRelation<V, in S : RelationEntity, in T : RelationEntity> : Relation<V, S, T>() {

    abstract val hasValue: Boolean

    internal abstract val internalValue: V
}

/**
 * Интерфейс сущности, которая участвкет в обмене данными.
 * Здесь происходит разделение интерфеса [Relation] для определенного [relationEntity]
 * Использует методы [bindTo] для подписки `consumer`a на текущий `observable`
 */
interface Related<S : RelationEntity> {

    fun relationEntity(): S

    /**
     * Метод который будет использоваться `bindTo` для подписки
     */
    fun <T> subscribe(observable: Observable<out T>,
                      onNext: Consumer<T>,
                      onError: (Throwable) -> Unit = { throw it }): Disposable

    /**
     * [Observable] для подписки. Доступен только для стороны получателя.
     */
    val <T> Relation<T, *, S>.observable: Observable<T>
        get() =
            this.getObservable(relationEntity())

    /**
     * [Consumer] для отправки значений. Доступен только для стороны отправителя.
     */

    val <T> Relation<T, S, *>.consumer: Consumer<T>
        get() =
            this.getConsumer(relationEntity())

    /**
     * Последний объект прошедший через [ValuableRelation]. Доступен только для стороны отправителя.
     * Для получателя значение доступно только через подписку.
     */
    val <T> ValuableRelation<T, S, *>.value: T
        get() =
            this.internalValue

    /**
     *  Послать [newValue] подписчикам
     */
    fun <T> Relation<T, S, *>.accept(newValue: T) =
            this.consumer.accept(newValue)

    /**
     *  Послать [Unit] подписчикам.
     *  @see accept
     */
    fun Relation<Unit, S, *>.accept() =
            this.consumer.accept(Unit)

    /**
     * Изменяет текущее значение на значение вычисленное в люмбде [block]
     */
    fun <T> ValuableRelation<T, S, *>.change(block: (T) -> (T)) =
            this.consumer.accept(block(internalValue))

    /*
     * Методы для подписки `consumer`a на текущий `observable`
     */

    infix fun <T> Observable<out T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this, consumer)

    infix fun <T> Observable<out T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.consumer)

    infix fun <T> Single<out T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this.toObservable(), consumer)

    infix fun <T> Single<out T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.consumer)

    infix fun <T> Maybe<out T>.bindTo(consumer: Consumer<T>) =
            this@Related.subscribe(this.toObservable(), consumer)

    infix fun <T> Maybe<out T>.bindTo(relation: Relation<T, S, *>) =
            this.bindTo(relation.consumer)

    infix fun Completable.bindTo(consumer: Consumer<Unit>) =
            this@Related.subscribe(this.toObservable(), consumer)

    infix fun Completable.bindTo(relation: Relation<Unit, S, *>) =
            this.bindTo(relation.consumer)

    infix fun <T> Observable<out T>.bindTo(consumer: (T) -> Unit) =
            this@Related.subscribe(this, Consumer { consumer(it) })

    infix fun Observable<Unit>.bindTo(consumer: () -> Unit) =
            this@Related.subscribe(this, Consumer { consumer() })

    fun <T> Observable<out T>.bindTo(consumer: (T) -> Unit, onError: (Throwable) -> Unit) =
            this@Related.subscribe(this, Consumer { consumer(it) }, onError)

    fun Observable<Unit>.bindTo(consumer: () -> Unit, onError: (Throwable) -> Unit) =
            this@Related.subscribe(this, Consumer { consumer() }, onError)

    fun <T> Single<out T>.bindTo(consumer: (T) -> Unit, onError: (Throwable) -> Unit) =
            this@Related.subscribe(this.toObservable(), Consumer { consumer(it) }, onError)

    fun <T> Maybe<out T>.bindTo(consumer: (T) -> Unit, onError: (Throwable) -> Unit) =
            this@Related.subscribe(this.toObservable(), Consumer { consumer(it) }, onError)

    fun Completable.bindTo(consumer: Consumer<Unit>, onError: (Throwable) -> Unit) =
            this@Related.subscribe(this.toObservable(), consumer, onError)

    infix fun <T> Relation<T, *, S>.bindTo(consumer: (T) -> Unit) =
            this.observable
                    .bindTo(consumer)

    infix fun Relation<Unit, *, S>.bindTo(consumer: () -> Unit) =
            this.observable
                    .bindTo(consumer)

    fun <T> Relation<T, *, S>.bindTo(consumer: (T) -> Unit, onError: (Throwable) -> Unit) =
            this.observable
                    .bindTo(consumer, onError)

    fun Relation<Unit, *, S>.bindTo(consumer: () -> Unit, onError: (Throwable) -> Unit) =
            this.observable
                    .bindTo(consumer, onError)

    infix fun <T> Relation<T, *, S>.bindTo(relation: Relation<T, S, *>) =
            this.observable.bindTo(relation.consumer)
}