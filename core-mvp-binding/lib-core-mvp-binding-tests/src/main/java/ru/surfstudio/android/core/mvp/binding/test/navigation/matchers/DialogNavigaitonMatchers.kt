package ru.surfstudio.android.core.mvp.binding.test.navigation.matchers

import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestDialogNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestDialogNavigationEvent.DismissDialog
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestDialogNavigationEvent.ShowDialog

/**
 * Проверка вызова метода [DialogNavigator.show]
 */
inline fun <reified RT : DialogRoute> TestDialogNavigationEvent<*>.shouldBeShowDialog(): ShowDialog<RT> {
    return shouldBeEventWithRoute<ShowDialog<RT>, RT>()
}

/**
 * Проверка вызова метода [DialogNavigator.dismiss]
 */
inline fun <reified RT : DialogRoute> TestDialogNavigationEvent<*>.shouldBeDismissDialog(): DismissDialog<RT> {
    return shouldBeEventWithRoute<DismissDialog<RT>, RT>()
}
