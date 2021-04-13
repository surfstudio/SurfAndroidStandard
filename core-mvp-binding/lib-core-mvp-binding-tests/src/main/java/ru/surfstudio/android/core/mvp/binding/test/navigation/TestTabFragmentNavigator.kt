package ru.surfstudio.android.core.mvp.binding.test.navigation

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.ui.event.back.OnBackPressedEvent
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.RootFragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent.ClearStack
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent.ClearTabs
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent.RouteEvent.*
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.BaseTestNavigator

class TestTabFragmentNavigator : BaseTestNavigator<TestTabFragmentNavigationEvent>(), TabFragmentNavigator {

    private val backPressedEventSubject = PublishSubject.create<OnBackPressedEvent>()
    override val backPressedEventObservable: Observable<OnBackPressedEvent> = backPressedEventSubject.hide()

    private val activeTabReOpenSubject = PublishSubject.create<Unit>()
    override val activeTabReOpenObservable: Observable<Unit> = activeTabReOpenSubject.hide()

    fun pressBack() {
        backPressedEventSubject.onNext(OnBackPressedEvent())
    }

    fun reopenActiveTab() {
        activeTabReOpenSubject.onNext(Unit)
    }

    override fun open(route: FragmentRoute) {
        mutableEvents.add(Open(route))
    }

    override fun <T> showAtTab(tabRoute: T, fragmentRoute: FragmentRoute, clearStack: Boolean) where T : FragmentRoute, T : RootFragmentRoute {
        mutableEvents.add(ShowAtTab(tabRoute, fragmentRoute, clearStack))
    }

    override fun replace(fragmentRoute: FragmentRoute) {
        mutableEvents.add(Replace(fragmentRoute))
    }

    override fun <T> clearTabs(vararg routes: T) where T : FragmentRoute, T : RootFragmentRoute {
        mutableEvents.add(ClearTabs(routes))
    }

    override fun clearStack() {
        mutableEvents.add(ClearStack)
    }

    override fun clearStackTo(route: FragmentRoute) {
        mutableEvents.add(ClearStackTo(route))
    }
}