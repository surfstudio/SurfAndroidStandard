package ru.surfstudio.android.core.mvp.binding.test.navigation

import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestFragmentNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestFragmentNavigationEvent.ClearBackStack
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestFragmentNavigationEvent.RouteEvent.*
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.BaseTestNavigator

/**
 * Тестовая реализация [FragmentNavigator]
 */
class TestFragmentNavigator : BaseTestNavigator<TestFragmentNavigationEvent>(), FragmentNavigator {

    override fun add(route: FragmentRoute, stackable: Boolean, transition: Int) {
        mutableEvents.add(Add(route))
    }

    override fun replace(route: FragmentRoute, stackable: Boolean, transition: Int) {
        mutableEvents.add(Replace(route))
    }

    override fun remove(route: FragmentRoute, transition: Int): Boolean {
        mutableEvents.add(Remove(route))
        return true
    }

    override fun show(route: FragmentRoute, transition: Int): Boolean {
        mutableEvents.add(Show(route))
        return true
    }

    override fun hide(route: FragmentRoute, transition: Int): Boolean {
        mutableEvents.add(Hide(route))
        return true
    }

    override fun popBackStack(): Boolean {
        mutableEvents.add(PopBackStack<FragmentRoute>())
        return true
    }

    override fun popBackStack(route: FragmentRoute, inclusive: Boolean): Boolean {
        mutableEvents.add(PopBackStack(route, inclusive))
        return true
    }

    override fun clearBackStack(): Boolean {
        mutableEvents.add(ClearBackStack)
        return true
    }
}