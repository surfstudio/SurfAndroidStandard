package ru.surfstudio.android.navigation.rx

import io.reactivex.Observable
import io.reactivex.Observer
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.listener.ScreenResultListener
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.rx.base.disposable.BaseNavigationDisposable
import java.io.Serializable

/**
 * Observable, that emits value when a screen result for [targetRoute]
 * is passed into [screenResultObserver].
 *
 * It will add [ScreenResultListener] to a [screenResultObserver] on subscribe and remove it on dispose.
 */
class ListenForScreenResultObservable<T : Serializable, R>(
    private val screenResultObserver: ScreenResultObserver,
    private val targetRoute: R
) : Observable<T>() where R : ResultRoute<T> {

    override fun subscribeActual(observer: Observer<in T>?) {
        requireNotNull(observer)
        val listener = Listener(screenResultObserver, observer, targetRoute)
        observer.onSubscribe(listener)
        screenResultObserver.addListener(targetRoute, listener)
    }

    private class Listener<T : Serializable, R>(
        private val screenResultObserver: ScreenResultObserver,
        private val observer: Observer<in T>,
        private val targetRoute: R
    ) : BaseNavigationDisposable(), ScreenResultListener<T> where R : ResultRoute<T> {

        override fun invoke(result: T) {
            observer.onNext(result)
        }

        override fun onDispose() {
            screenResultObserver.removeListener(targetRoute)
        }
    }
}