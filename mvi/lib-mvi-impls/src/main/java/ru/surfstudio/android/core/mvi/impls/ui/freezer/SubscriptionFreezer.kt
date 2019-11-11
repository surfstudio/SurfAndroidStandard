package ru.surfstudio.android.core.mvi.impls.ui.freezer

import com.agna.ferro.rx.ObservableOperatorFreeze
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Класс, отвечающий за заморозку/разморозку Observable в случаях,
 * когда consumer не должен реагировать на события
 */
abstract class SubscriptionFreezer {

    abstract val freezeSelector: BehaviorSubject<Boolean>

    open fun <T> freeze(
            observable: Observable<T>,
            replaceFrozenPredicate: (t1: T, t2: T) -> Boolean = { _, _ -> false }
    ): Observable<T> =
            observable.lift(
                    ObservableOperatorFreeze(
                            freezeSelector,
                            replaceFrozenPredicate
                    )
            )
}

/**
 * Заморозка с помощью [SubscriptionFreezer].
 */
fun <T> Observable<T>.freeze(
        subscriptionFreezer: SubscriptionFreezer,
        replaceFrozenPredicate: (t1: T, t2: T) -> Boolean = { _, _ -> false }
) = subscriptionFreezer.freeze(this, replaceFrozenPredicate)