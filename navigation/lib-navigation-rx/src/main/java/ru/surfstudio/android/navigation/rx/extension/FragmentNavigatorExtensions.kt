package ru.surfstudio.android.navigation.rx.extension

import io.reactivex.Observable
import ru.surfstudio.android.navigation.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.rx.FragmentBackStackChangedObservable

fun FragmentNavigatorInterface.backStackChanges(): Observable<FragmentBackStack> {
    return FragmentBackStackChangedObservable(this)
}