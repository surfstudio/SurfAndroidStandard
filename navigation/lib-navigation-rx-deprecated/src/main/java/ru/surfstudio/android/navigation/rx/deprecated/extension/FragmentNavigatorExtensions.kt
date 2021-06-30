package ru.surfstudio.android.navigation.rx.deprecated.extension

import io.reactivex.Observable
import ru.surfstudio.android.navigation.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.rx.deprecated.FragmentBackStackChangedObservable

/**
 * Get an updated [FragmentBackStack] snapshot for every operation that changes it.
 */
fun FragmentNavigatorInterface.observeBackStackChanged(): Observable<FragmentBackStack> {
    return FragmentBackStackChangedObservable(this)
}