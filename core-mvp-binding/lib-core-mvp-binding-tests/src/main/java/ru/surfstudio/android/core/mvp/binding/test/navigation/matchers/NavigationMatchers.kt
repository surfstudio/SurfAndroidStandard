package ru.surfstudio.android.core.mvp.binding.test.navigation.matchers

import io.kotest.matchers.types.shouldBeTypeOf
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.RouteEventInterface

inline fun <reified ET : RouteEventInterface<RT>, reified RT : Route> TestNavigationEvent.shouldBeEventWithRoute(): ET {
    return shouldBeTypeOf<ET>().also {
        it.route.shouldBeTypeOf<RT>()
    }
}
