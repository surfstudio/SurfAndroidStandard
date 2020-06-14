package ru.surfstudio.android.navigation.rx.extension

import io.reactivex.Observable
import ru.surfstudio.android.navigation.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface
import ru.surfstudio.android.navigation.rx.ActiveTabReopenedObservable
import ru.surfstudio.android.navigation.rx.FragmentBackStackChangedObservable

fun TabFragmentNavigatorInterface.activeTabReopens(): Observable<String> {
    return ActiveTabReopenedObservable(this)
}