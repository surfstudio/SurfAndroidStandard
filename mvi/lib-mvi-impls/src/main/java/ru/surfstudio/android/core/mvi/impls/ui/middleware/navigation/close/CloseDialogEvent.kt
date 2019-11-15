package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute

/**
 * Closes dialog with specific [route]
 */
interface CloseDialogEvent : CloseScreenEvent {
    val route: DialogRoute
}