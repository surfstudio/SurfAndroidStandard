package ru.surfstudio.android.core.mvp.binding.test.navigation

import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestDialogNavigationEvent
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestDialogNavigationEvent.DismissDialog
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.TestNavigationEvent.TestDialogNavigationEvent.ShowDialog
import ru.surfstudio.android.core.mvp.binding.test.navigation.base.BaseTestNavigator

/**
 * Тестовая реализация [DialogNavigator]
 */
class TestDialogNavigator : BaseTestNavigator<TestDialogNavigationEvent<*>>(), DialogNavigator {

    override fun show(dialogRoute: DialogRoute) {
        mutableEvents.add(ShowDialog(dialogRoute))
    }

    override fun dismiss(dialogRoute: DialogRoute) {
        mutableEvents.add(DismissDialog(dialogRoute))
    }

}