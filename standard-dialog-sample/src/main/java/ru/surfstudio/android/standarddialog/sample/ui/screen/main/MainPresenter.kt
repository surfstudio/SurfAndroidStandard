package ru.surfstudio.android.standarddialog.sample.ui.screen.main

import androidx.core.widget.toast
import com.example.standarddialog.StandardDialogPresenter
import com.example.standarddialog.StandardDialogRoute
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.standarddialog.sample.R
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val dialogNavigator: DialogNavigator
) : BasePresenter<MainActivityView>(basePresenterDependency), StandardDialogPresenter {

    private val screenModel: MainScreenModel = MainScreenModel()

    companion object {
        private const val FIRST_DIALOG_TAG = "FIRST_DIALOG"
        private const val SECOND_DIALOG_TAG = "SECOND_DIALOG"
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    override fun simpleDialogPositiveBtnAction(dialogTag: String) {
        showToast(
                dialogTag,
                "first dialog accepted",
                "second dialog accepted")
    }

    override fun simpleDialogNegativeBtnAction(dialogTag: String) {
        showToast(
                dialogTag,
                "first dialog canceled",
                "second dialog canceled")
    }

    fun showFirstDialog() {
        dialogNavigator.show(StandardDialogRoute(
                title = "First dialog title",
                message = "First dialog message",
                positiveBtnText = "yes",
                negativeBtnText = "no",
                dialogTag = FIRST_DIALOG_TAG))
    }

    fun showSecondDialog() {
        dialogNavigator.show(StandardDialogRoute(
                titleRes = R.string.second_dialog_title,
                messageRes = R.string.second_dialog_message,
                dialogTag = SECOND_DIALOG_TAG))
    }

    private fun showToast(dialogTag: String,
                          firstDialogMessage: String,
                          secondDialogMessage: String) {
        when (dialogTag) {
            FIRST_DIALOG_TAG -> view.toast(firstDialogMessage)
            SECOND_DIALOG_TAG -> view.toast(secondDialogMessage)
        }
    }
}