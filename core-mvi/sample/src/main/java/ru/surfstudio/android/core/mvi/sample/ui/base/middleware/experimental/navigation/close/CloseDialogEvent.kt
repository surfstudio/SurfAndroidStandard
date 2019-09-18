package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.close

import ru.surfstudio.android.mvp.dialog.navigation.route.DialogRoute

interface CloseDialogEvent : CloseScreenEvent {
    val route: DialogRoute
}