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
import ru.surfstudio.android.core.mvp.binding.rx.relation.BehaviorRelation

/**
 * Связь Presenter -> Presenter
 *
 * С осторожностью: презентер сообщающий значение не должен быть на него подписан
 *
 * Хранит в себе последнее прошедшее значение.
 * При подписке сообщает это значение или initialValue
 */
@Deprecated("После возможности подписки на State презентором, потерял свою актуальность")
class Complice<T>(initialValue: T? = null) : BehaviorRelation<T, PRESENTER, PRESENTER>(initialValue) {

    override fun getConsumer(source: PRESENTER): Consumer<T> = relay

    override fun getObservable(target: PRESENTER): Observable<T> = relay.share()
}