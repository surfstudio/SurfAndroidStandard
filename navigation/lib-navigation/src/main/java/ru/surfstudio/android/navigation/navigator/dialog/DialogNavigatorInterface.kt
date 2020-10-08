package ru.surfstudio.android.navigation.navigator.dialog

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

interface DialogNavigatorInterface {

    fun show(route: DialogRoute, animations: Animations)

    fun dismiss(route: DialogRoute, animations: Animations)
}