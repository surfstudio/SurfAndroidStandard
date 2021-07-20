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
package ru.surfstudio.android.core.mvi.impls.ui.freezer

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Empty subscription freezer without freeze effect on subscriptions.
 *
 * If it is used in ScreenBinder, the observable chain wont be frozen,
 * and its lifecycle management is all up to you.
 */
class EmptySubscriptionFreezer : SubscriptionFreezer() {

    companion object {
        const val TAG = "EmptySubscriptionFreezer"
    }

    override val freezeSelector: BehaviorSubject<Boolean> = BehaviorSubject.create()

    override fun <T> freeze(
            observable: Observable<T>,
            replaceFrozenPredicate: (t1: T, t2: T) -> Boolean
    ): Observable<T> {
        return observable
    }
}