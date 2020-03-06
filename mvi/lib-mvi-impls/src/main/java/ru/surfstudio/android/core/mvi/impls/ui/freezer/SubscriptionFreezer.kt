package ru.surfstudio.android.core.mvi.impls.ui.freezer

import com.agna.ferro.rx.ObservableOperatorFreeze
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Object that freezes observable subscriptions when the consumer shouldn't react on changes.
 */
abstract class SubscriptionFreezer {

    /**
     * Selector which is responsible for freezing.
     * When it's accepting [true] in 'onNext' callback, it will freeze events.
     * When the [false] is accepted, all unhandled events will be pushed to subscribers.
     * */
    abstract val freezeSelector: BehaviorSubject<Boolean>

    /**
     * Freeze observable chain with [ObservableOperatorFreeze] and [freezeSelector].
     *
     * @param observable observable to freeze
     * @param replaceFrozenPredicate    predicate to determine whether to replace new values of subscription or not.
     *                                  You need to pass 'true' to keep only last value.
     *                                  By default, it wont replace any values.
     */
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
 * Freeze an observable with [SubscriptionFreezer].
 */
fun <T> Observable<T>.freeze(
        subscriptionFreezer: SubscriptionFreezer,
        replaceFrozenPredicate: (t1: T, t2: T) -> Boolean = { _, _ -> false }
) = subscriptionFreezer.freeze(this, replaceFrozenPredicate)