package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close

import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.ExperimentalFeature
import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute

/**
 * Событие закрытие диалога
 */
@ExperimentalFeature
interface CloseDialogEvent : CloseScreenEvent {
    val route: DialogRoute
}