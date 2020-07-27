package ru.surfstudio.android.standarddialog.sample.ui.screen.main

import ru.surfstudio.android.standarddialog.StandardDialogPresenter
import ru.surfstudio.android.standarddialog.StandardDialogRoute
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.sample.dagger.ui.base.StringsProvider
import ru.surfstudio.android.standarddialog.sample.R
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val dialogNavigator: DialogNavigator,
        private val stringsProvider: StringsProvider
) : BasePresenter<MainActivityView>(basePresenterDependency), StandardDialogPresenter {

    private val sm: MainScreenModel = MainScreenModel()

    companion object {
        private const val FIRST_DIALOG_TAG = "FIRST_DIALOG"
        private const val SECOND_DIALOG_TAG = "SECOND_DIALOG"
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    override fun simpleDialogPositiveBtnAction(dialogTag: String) {
        showMessage(
                dialogTag,
                stringsProvider.getString(R.string.first_dialog_accepted),
                stringsProvider.getString(R.string.second_dialog_accepted)
        )
    }

    override fun simpleDialogNegativeBtnAction(dialogTag: String) {
        showMessage(
                dialogTag,
                stringsProvider.getString(R.string.first_dialog_canceled),
                stringsProvider.getString(R.string.second_dialog_canceled)
        )
    }

    fun showFirstDialog() {
        dialogNavigator.show(StandardDialogRoute(
                title = stringsProvider.getString(R.string.first_dialog_title),
                message = stringsProvider.getString(R.string.first_dialog_message),
                positiveBtnText = "yes",
                negativeBtnText = "no",
                dialogTag = FIRST_DIALOG_TAG)
        )
    }

    fun showSecondDialog() {
        dialogNavigator.show(StandardDialogRoute(
                titleRes = R.string.second_dialog_title,
                messageRes = R.string.second_dialog_message,
                dialogTag = SECOND_DIALOG_TAG)
        )
    }

    private fun showMessage(
            dialogTag: String,
            firstDialogMessage: String,
            secondDialogMessage: String
    ) {
        when (dialogTag) {
            FIRST_DIALOG_TAG -> view.showMessage(firstDialogMessage)
            SECOND_DIALOG_TAG -> view.showMessage(secondDialogMessage)
        }
    }
}