package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.close

import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute

/**
 * Событие закрытие диалога
 */
interface CloseDialogEvent : CloseScreenEvent {
    val route: DialogRoute
}