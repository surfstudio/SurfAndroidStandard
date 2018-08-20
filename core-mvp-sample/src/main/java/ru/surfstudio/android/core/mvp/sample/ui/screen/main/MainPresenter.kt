package ru.surfstudio.android.core.mvp.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.sample.R
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            view.persistentScope.screenEventDelegateManager.registerDelegate(
                    OnBackPressedDelegate {
                        messageController.show(
                                stringId = R.string.snackbar_message,
                                actionStringId = R.string.close_app,
                                listener = { finish() })
                        true
                    })
        } else {
            view.render(screenModel)
        }
    }
}