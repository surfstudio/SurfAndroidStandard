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

package ru.surfstudio.android.core.mvp.rx.relation

import com.jakewharton.rxrelay2.BehaviorRelay

/**
 * Связь хранящая в себе последнее прошедшее значение.
 * При подписке сообщает это значение или initialValue
 */
abstract class ValuableRelation<T, in S : RelationEntity, in D : RelationEntity>(initialValue: T? = null)
    : Relation<T, S, D>() {

    protected val relay = initialValue?.let { BehaviorRelay.createDefault(it) }
            ?: BehaviorRelay.create<T>()

    val hasValue: Boolean get() = relay.hasValue()

    internal val internalValue: T get() = relay.value ?: throw NoSuchElementException()
}