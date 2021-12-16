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

package ru.surfstudio.android.core.mvp.binding.rx.relation.mvp

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.relation.ValuableRelation
import java.util.concurrent.atomic.AtomicReference

/**
 * Связь View -> Presenter
 *      Presenter -> View
 *
 * Хранит в себе последнее прошедшее значение.
 * При подписке сообщает это значение или initialValue
 */
@Deprecated("Use Action/State pair instead")
class Bond<T>(initialValue: T? = null) : ValuableRelation<T, BondSource, BondTarget>() {

    private val action = initialValue?.let { Action<T>(it) } ?: Action()
    private val command = initialValue?.let { State<T>(it) } ?: State()

    private val cachedValue = AtomicReference<T>()

    override val internalValue: T get() = cachedValue.get()
    override val hasValue: Boolean get() = cachedValue.get() != null

    override fun getConsumer(source: BondSource): Consumer<T> =
            when (source) {
                is VIEW -> action.getConsumer(source)
                is PRESENTER -> command.getConsumer(source)
                else -> throw IllegalArgumentException("Illegal relationEntity $source")
            }

    override fun getObservable(target: BondTarget): Observable<T> =
            when (target) {
                is PRESENTER -> action.getObservable(target)
                is VIEW -> command.getObservable(target)
                else -> throw IllegalArgumentException("Illegal relationEntity $target")
            }
                    .doOnNext(cachedValue::set)
}