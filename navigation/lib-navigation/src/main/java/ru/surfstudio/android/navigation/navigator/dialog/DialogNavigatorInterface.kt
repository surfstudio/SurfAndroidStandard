package ru.surfstudio.android.navigation.navigator.dialog

import ru.surfstudio.android.navigation.route.dialog.DialogRoute

interface DialogNavigatorInterface {

    fun show(route: DialogRoute)

    fun dismiss(route: DialogRoute)
}