package ru.surfstudio.android.mvp.dialog.navigation.navigator

import ru.surfstudio.android.core.ui.navigation.Navigator
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute

/**
 * позволяет открывать диалоги
 */
interface DialogNavigator : Navigator {

    fun show(dialogRoute: DialogRoute)

    fun dismiss(dialogRoute: DialogRoute)
}