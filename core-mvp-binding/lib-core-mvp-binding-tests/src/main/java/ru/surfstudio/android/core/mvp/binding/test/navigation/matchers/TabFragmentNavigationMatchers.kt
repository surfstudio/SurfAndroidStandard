package ru.surfstudio.android.core.mvp.binding.test.navigation.matchers

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent.ClearStack
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent.ClearTabs
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestTabFragmentNavigationEvent.RouteEvent.*
import kotlin.reflect.KClass

/**
 * Проверка вызова метода [TabFragmentNavigator.open]
 */
inline fun <reified RT : FragmentRoute> TestTabFragmentNavigationEvent.shouldBeOpen(): Open<RT> {
    return shouldBeEventWithRoute<Open<RT>, RT>()
}

/**
 * Проверка вызова метода [TabFragmentNavigator.showAtTab]
 * @param TRT класс роута таба
 * @param RT класс роута экрана
 */
inline fun <reified TRT : FragmentRoute, reified RT : FragmentRoute> TestTabFragmentNavigationEvent.shouldBeShowAtTab(): ShowAtTab<RT> {
    return shouldBeEventWithRoute<ShowAtTab<RT>, RT>().also {
        it.tabRoute.shouldBeTypeOf<TRT>()
    }
}

/**
 * Проверка вызова метода [TabFragmentNavigator.clearTabs]
 * @param expectedRoutesToClean множество роутов табов, которые должны быть очищены
 */
inline fun <reified RT : Route> TestTabFragmentNavigationEvent.shouldBeClearTabs(expectedRoutesToClean: Set<KClass<out RT>>): ClearTabs {
    val event = shouldBeTypeOf<ClearTabs>()
    val routesClasses = event.routes.map { it::class }.toSet()
    routesClasses shouldBe expectedRoutesToClean
    return event
}

/**
 * Проверка вызова метода [TabFragmentNavigator.clearStackTo]
 */
inline fun <reified RT : FragmentRoute> TestTabFragmentNavigationEvent.shouldBeClearStackTo(): ClearStackTo<RT> {
    return shouldBeEventWithRoute<ClearStackTo<RT>, RT>()
}

/**
 * Проверка вызова метода [TabFragmentNavigator.replace]
 */
inline fun <reified RT : FragmentRoute> TestTabFragmentNavigationEvent.shouldBeReplace(): Replace<RT> {
    return shouldBeEventWithRoute<Replace<RT>, RT>()
}

/**
 * Проверка вызова метода [TabFragmentNavigator.clearStack]
 */
fun TestTabFragmentNavigationEvent.shouldBeClearStack(): ClearStack {
    return shouldBeTypeOf<ClearStack>()
}
