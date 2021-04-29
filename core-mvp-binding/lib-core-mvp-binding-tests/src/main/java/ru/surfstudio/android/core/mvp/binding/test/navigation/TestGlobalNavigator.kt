package ru.surfstudio.android.core.mvp.binding.test.navigation

import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent.RouteEvent.Start
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.BaseTestNavigator

/**
 * Тестовая реализация [GlobalNavigator]
 */
class TestGlobalNavigator : BaseTestNavigator<TestActivityNavigationEvent>(), GlobalNavigator  {

    override fun start(route: ActivityRoute): Boolean {
        mutableEvents.add(Start(route))
        return true
    }
}