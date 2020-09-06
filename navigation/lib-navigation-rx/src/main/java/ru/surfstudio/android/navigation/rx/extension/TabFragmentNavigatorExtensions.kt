package ru.surfstudio.android.navigation.rx.extension

import io.reactivex.Observable
import ru.surfstudio.android.navigation.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.rx.ActiveTabReopenedObservable
import ru.surfstudio.android.navigation.rx.FragmentBackStackChangedObservable
import ru.surfstudio.android.navigation.rx.TabHeadChangedObservable

/**
 * Get all events at which currently active and empty tab is reopened.
 *
 * @return [Observable] with current tab's tag.
 */
fun TabFragmentNavigatorInterface.observeActiveTabReopened(): Observable<String> {
    return ActiveTabReopenedObservable(this)
}

/**
 * Get all events with active tab head changes.
 *
 * @return [Observable] with current head's route.
 */
fun TabFragmentNavigatorInterface.observeTabHeadChanged(): Observable<FragmentRoute> {
    return TabHeadChangedObservable(this)
}