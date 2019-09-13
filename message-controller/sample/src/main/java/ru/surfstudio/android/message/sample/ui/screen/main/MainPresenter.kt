package ru.surfstudio.android.message.sample.ui.screen.main

import android.view.Gravity
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.message.sample.R
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun showColoredSnackbar() = messageController.show(
            R.string.snackbar_message,
            R.color.colorAccent)

    fun showSnackbarWithDuration() = messageController.show(
            stringId = R.string.snackbar_message,
            duration = 5000)

    fun showSnackbarWithListener() = messageController.show(
            stringId = R.string.snackbar_message,
            actionStringId = R.string.snackbar_action,
            buttonColor = R.color.colorPrimary,
            listener = { messageController.showToast(R.string.toast_action) })

    fun showGravityToast() = messageController.showToast(R.string.toast_message, Gravity.CENTER)

    fun closeSnackbar() = messageController.closeSnack()
}