package ru.surfstudio.android.navigation.rx

import io.reactivex.Observable
import io.reactivex.Observer
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.ActiveTabReopenedListener
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.TabHeadChangedListener
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.rx.base.disposable.BaseNavigationDisposable

/**
 * Observable, that emits value after [TabFragmentNavigatorInterface]'s tab head is changed.
 *
 * The parameter of this observable will be current tab head route.
 */
class TabHeadChangedObservable(
        private val navigator: TabFragmentNavigatorInterface
) : Observable<FragmentRoute>() {

    override fun subscribeActual(observer: Observer<in FragmentRoute>?) {
        requireNotNull(observer)
        val listener = Listener(navigator, observer)
        observer.onSubscribe(listener)
        navigator.addTabHeadChangedListener(listener)
    }

    private class Listener(
            private val navigator: TabFragmentNavigatorInterface,
            private val observer: Observer<in FragmentRoute>
    ) : BaseNavigationDisposable(), TabHeadChangedListener {

        override fun invoke(route: FragmentRoute) {
            observer.onNext(route)
        }

        override fun onDispose() {
            navigator.removeTabHeadChangedListener(this)
        }
    }
}