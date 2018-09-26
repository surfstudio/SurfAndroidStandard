package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.storage.FcmStorage
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.util.ClipboardManagerHelper
import javax.inject.Inject

/**
 * Презентер экрана показа fcm-токена
 */
@PerScreen
class DebugFcmPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                            private val stringsProvider: StringsProvider,
                                            private val fcmStorage: FcmStorage,
                                            private val clipboardManagerHelper: ClipboardManagerHelper
) : BasePresenter<DebugFcmActivityView>(basePresenterDependency) {

    private val screenModel = DebugFcmScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            loadFcmToken()
        } else {
            view.render(screenModel)
        }
    }

    fun loadFcmToken() {
        val fcmToken = fcmStorage.fcmToken
        screenModel.fcmToken = fcmToken
        screenModel.loadState = if (fcmToken.isEmpty()) LoadState.EMPTY else LoadState.NONE
        view.render(screenModel)
        logFcmToken()
    }

    fun copyFcmToken() {
        screenModel.fcmToken?.let {
            clipboardManagerHelper.copyString(it)
            view.showMessage(stringsProvider.getString(R.string.fcm_copied_message))
            logFcmToken()
        }
    }

    private fun logFcmToken() = Logger.d("FCM-token: ${screenModel.fcmToken}")
}
