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
package ru.surfstudio.android.core.mvi.impls.ui.binder

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.observers.LambdaObserver
import ru.surfstudio.android.core.mvi.impls.ui.freezer.SubscriptionFreezer
import ru.surfstudio.android.core.mvi.impls.ui.freezer.freeze
import ru.surfstudio.android.core.mvi.ui.binder.RxBinder
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate
import ru.surfstudio.android.rx.extension.ObservableUtil

/**
 * Custom [RxBinder] implementation.
 *
 * Supports subscription freezing through [SubscriptionFreezer].
 */
class ScreenBinder(
        screenBinderDependency: ScreenBinderDependency
) : RxBinder, OnCompletelyDestroyDelegate {


    init {
        screenBinderDependency.eventDelegateManager.registerDelegate(this)
    }

    var subscriptionFreezer: SubscriptionFreezer = screenBinderDependency.subscriptionFreezer
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun <T> subscribe(
            observable: Observable<T>,
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit
    ): Disposable {
        val observer = LambdaObserver<T>(
                Consumer(onNext),
                Consumer(onError),
                ObservableUtil.EMPTY_ACTION,
                Functions.emptyConsumer()
        )

        val disposable = observable
                .freeze(subscriptionFreezer)
                .subscribeWith(observer)

        disposables.add(disposable)
        return disposable
    }

    override fun onCompletelyDestroy() {
        disposables.dispose()
    }
}