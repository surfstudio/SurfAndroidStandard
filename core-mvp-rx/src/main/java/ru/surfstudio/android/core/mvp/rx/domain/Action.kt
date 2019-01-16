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
 * Rx-обертка над действиями пользователя
 * За отправку событий отвечает View
 * Подписка на события происходит только внутри Presenter
 */
class Action<T> : Relation<T, VIEW, PRESENTER> {

    private val relay = BehaviorRelay.create<T>()

    override val hasValue: Boolean get() = relay.hasValue()

    override val value: T get() = relay.value ?: throw NoSuchElementException()

    override fun getConsumer(source: VIEW): Consumer<T> = relay

    override fun getObservable(target: PRESENTER): Observable<T> = relay.distinctUntilChanged()
}