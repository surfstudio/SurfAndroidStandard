package ru.surfstudio.android.navigation.rx.deprecated

import io.reactivex.Observable
import io.reactivex.Observer
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.ActiveTabReopenedListener
import ru.surfstudio.android.navigation.rx.deprecated.base.disposable.BaseNavigationDisposable

/**
 * Observable, that emits value after [TabFragmentNavigatorInterface]'s active tab is reopened again.
 *
 * The parameter of this observable will be current tab's tag.
 */
@Deprecated("Prefer using new implementation")
class ActiveTabReopenedObservable(
        private val navigator: TabFragmentNavigatorInterface
) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>?) {
        requireNotNull(observer)
        val listener = Listener(navigator, observer)
        observer.onSubscribe(listener)
        navigator.setActiveTabReopenedListener(listener)
    }

    private class Listener(
            private val navigator: TabFragmentNavigatorInterface,
            private val observer: Observer<in String>
    ) : BaseNavigationDisposable(), ActiveTabReopenedListener {

        override fun invoke(stack: String) {
            observer.onNext(stack)
        }

        override fun onDispose() {
            navigator.setActiveTabReopenedListener(null)
        }
    }
}