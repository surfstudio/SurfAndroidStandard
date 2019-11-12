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