package ru.surfstudio.android.core.mvi.impls.ui.freezer

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Не замораживает Observable
 *
 * Чтобы использовать EmptySubscriptionFreezer на экране, необходимо проставить аннотацию
 * @Named(EmptySubscriptionFreezer.TAG) у поля [ScreenBinderDependency] в конфигураторе экрана.
 *
 * После этого, управление заморозкой подписок полностью ложится на вас.
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