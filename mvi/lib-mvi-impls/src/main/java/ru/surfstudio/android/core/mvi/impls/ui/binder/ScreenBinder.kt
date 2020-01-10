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