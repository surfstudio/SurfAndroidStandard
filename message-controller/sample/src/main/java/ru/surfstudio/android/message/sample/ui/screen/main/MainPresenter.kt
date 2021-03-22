package ru.surfstudio.android.message.sample.ui.screen.main

import android.view.Gravity
import android.widget.Toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.message.sample.R
import ru.surfstudio.android.message.sample.ui.screen.message.MessageActivityRoute
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val messageController: MessageController,
        private val resourceProvider: ResourceProvider
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
            messageResId = R.string.snackbar_message,
            duration = 5000)

    fun showSnackbarWithListener() = messageController.show(
            messageResId = R.string.snackbar_message,
            actionResId = R.string.snackbar_action,
            actionColorResId = R.color.colorPrimary,
            listener = { messageController.showToast(R.string.toast_action) })

    fun showSnackbarWithDismissListener() = messageController.show(
            messageResId = R.string.snackbar_message,
            actionResId = R.string.snackbar_action,
            actionColorResId = R.color.colorPrimary,
            dismissListener = { reason ->
                messageController.showToast(
                        message = resourceProvider.getString(R.string.snackbar_dismiss, reason),
                        duration = Toast.LENGTH_SHORT
                )
            }
    )

    fun showGravityToast() = messageController.showToast(R.string.toast_message, Gravity.CENTER)

    fun closeSnackbar() = messageController.closeSnack()

    fun startMessageDemo() = activityNavigator.start(MessageActivityRoute())
}