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

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Rx-обертка над командами для View
 *
 * За отправку событий отвечает Presenter
 * Подписывается на события View
 */
class State<T>(initialValue: T? = null) : Relation<T, PRESENTER, VIEW> {

    private val relay = initialValue?.let { BehaviorRelay.createDefault(it) }
            ?: BehaviorRelay.create<T>()

    override val hasValue: Boolean get() = relay.hasValue()

    override val value: T get() = relay.value ?: throw NoSuchElementException()

    override fun getConsumer(source: PRESENTER): Consumer<T> = relay

    override fun getObservable(target: VIEW): Observable<T> = relay.share()
}