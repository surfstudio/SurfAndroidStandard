package ru.surfstudio.android.core.mvp.binding.test.navigation.matchers

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent.FinishAffinity
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent.FinishCurrent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestActivityNavigationEvent.RouteEvent.*
import ru.surfstudio.android.core.ui.navigation.ActivityRouteInterface
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.navigation.event.result.SupportOnActivityResultRoute
import java.io.Serializable
import kotlin.reflect.KClass

/**
 * Проверка вызова метода [ActivityNavigator.start] или [GlobalNavigator.start]
 */
inline fun <reified RT : ActivityRouteInterface> TestActivityNavigationEvent.shouldBeStart(): Start<RT> {
    return shouldBeEventWithRoute<Start<RT>, RT>()
}

/**
 * Проверка вызова метода [ActivityNavigator.startForResult]
 */
inline fun <reified RT : ActivityRouteInterface> TestActivityNavigationEvent.shouldBeStartForResult(): StartForResult<RT> {
    return shouldBeEventWithRoute<StartForResult<RT>, RT>()
}

/**
 * Проверка вызова метода [ActivityNavigator.finishWithResult]
 */
inline fun <reified RT : SupportOnActivityResultRoute<T>, T : Serializable> TestActivityNavigationEvent.shouldBeFinishWithResult(routeClass: KClass<RT>): FinishWithResult<RT, T> {
    return shouldBeEventWithRoute<FinishWithResult<RT, T>, RT>()
}

/**
 * Проверка, что активити завершилась с успехом
 */
fun <RT : SupportOnActivityResultRoute<T>, T : Serializable> FinishWithResult<RT, T>.shouldBeSuccess() = apply {
    success shouldBe true
}

/**
 * Проверка результата активити
 */
fun <RT : SupportOnActivityResultRoute<T>, T : Serializable> FinishWithResult<RT, T>.withResult(result: T?) = apply {
    this.result shouldBe result
}

/**
 * Проверка вызова метода [ActivityNavigator.finishCurrent]
 */

fun TestActivityNavigationEvent.shouldBeFinishCurrent(): FinishCurrent {
    return shouldBeTypeOf<FinishCurrent>()
}

/**
 * Проверка вызова метода [ActivityNavigator.finishAffinity]
 */
fun TestActivityNavigationEvent.shouldBeFinishAffinity(): FinishAffinity {
    return shouldBeTypeOf<FinishAffinity>()
}
