package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.result

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import javax.inject.Inject

const val DIALOG_RESULT = "DIALOG_RESULT"

@PerScreen
class ResultDialogPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val dialogNavigator: DialogNavigator,
        private val route: ResultDialogRoute
) : BasePresenter<ResultDialogView>(
        basePresenterDependency
) {

    fun finishWithResult() {
        dialogNavigator.dismissWithResult(route, DIALOG_RESULT, true)
    }
}