package ru.surfstudio.android.navigation.rx.deprecated

import io.reactivex.Observable
import io.reactivex.Observer
import ru.surfstudio.android.navigation.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.backstack.fragment.listener.FragmentBackStackChangedListener
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.rx.deprecated.base.disposable.BaseNavigationDisposable

/**
 * Observable, that emits value after [FragmentNavigatorInterface]'s BackStack is changed.
 *
 * It will add [FragmentBackStackChangedListener] to navigator on subscribe and remove it on dispose.
 */
class FragmentBackStackChangedObservable(
        private val navigator: FragmentNavigatorInterface
) : Observable<FragmentBackStack>() {

    override fun subscribeActual(observer: Observer<in FragmentBackStack>?) {
        requireNotNull(observer)
        val listener = Listener(navigator, observer)
        observer.onSubscribe(listener)
        navigator.addBackStackChangeListener(listener)
    }

    private class Listener(
            private val navigator: FragmentNavigatorInterface,
            private val observer: Observer<in FragmentBackStack>
    ) : BaseNavigationDisposable(), FragmentBackStackChangedListener {

        override fun invoke(stack: FragmentBackStack) {
            observer.onNext(stack)
        }

        override fun onDispose() {
            navigator.removeBackStackChangeListener(this)
        }
    }
}