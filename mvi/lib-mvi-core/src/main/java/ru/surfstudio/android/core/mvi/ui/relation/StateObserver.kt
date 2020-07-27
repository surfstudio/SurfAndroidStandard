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
package ru.surfstudio.android.core.mvi.ui.relation

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW

/**
 * Класс, который может извлекать [Observable] из State, но не может подписываться на него.
 */
interface StateObserver : Related<VIEW> {

    override fun relationEntity() = VIEW

    @CallSuper
    override fun <T> subscribe(observable: Observable<out T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw NotImplementedError("StateEmitter cant manage subscription lifecycle")
    }
}