package ru.surfstudio.android.core.mvp.binding.test.navigation.matchers

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestFragmentNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestFragmentNavigationEvent.ClearBackStack
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestFragmentNavigationEvent.RouteEvent.*


/**
 * Проверка вызова метода [FragmentNavigator.add]
 */
inline fun <reified RT : FragmentRoute> TestFragmentNavigationEvent.shouldBeAdd(): Add<RT> {
    return shouldBeEventWithRoute<Add<RT>, RT>()
}

/**
 * Проверка вызова метода [FragmentNavigator.replace]
 */
inline fun <reified RT : FragmentRoute> TestFragmentNavigationEvent.shouldBeReplace(): Replace<RT> {
    return shouldBeEventWithRoute<Replace<RT>, RT>()
}

/**
 * Проверка вызова метода [FragmentNavigator.remove]
 */
inline fun <reified RT : FragmentRoute> TestFragmentNavigationEvent.shouldBeRemove(): Remove<RT> {
    return shouldBeEventWithRoute<Remove<RT>, RT>()
}

/**
 * Проверка вызова метода [FragmentNavigator.show]
 */
inline fun <reified RT : FragmentRoute> TestFragmentNavigationEvent.shouldBeShow(): Show<RT> {
    return shouldBeEventWithRoute<Show<RT>, RT>()
}

/**
 * Проверка вызова метода [FragmentNavigator.hide]
 */
inline fun <reified RT : FragmentRoute> TestFragmentNavigationEvent.shouldBeHide(): Hide<RT> {
    return shouldBeEventWithRoute<Hide<RT>, RT>()
}

/**
 * Проверка вызова метода [FragmentNavigator.popBackStack]
 */
inline fun <reified RT : FragmentRoute> TestFragmentNavigationEvent.shouldBePopBackStack(inclusive: Boolean = true): PopBackStack<RT> {
    return shouldBeEventWithRoute<PopBackStack<RT>, RT>().also {
        it.inclusive shouldBe inclusive
    }
}

/**
 * Проверка вызова метода [FragmentNavigator.clearBackStack]
 */
fun TestFragmentNavigationEvent.shouldBeClearBackStack(): ClearBackStack {
    return shouldBeTypeOf<ClearBackStack>()
}
