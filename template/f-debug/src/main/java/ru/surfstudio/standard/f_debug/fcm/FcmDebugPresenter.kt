package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.storage.FcmStorage
import ru.surfstudio.android.template.f_debug.R
import javax.inject.Inject

/**
 * Презентер экрана показа fcm-токена
 */
@PerScreen
class FcmDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val stringsProvider: StringsProvider,
        private val fcmStorage: FcmStorage
) : BasePresenter<FcmDebugActivityView>(basePresenterDependency) {

    private val screenModel = FcmDebugScreenModel()

    override fun onFirstLoad() {
        super.onFirstLoad()
        loadFcmToken()
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun loadFcmToken() {
        screenModel.fcmToken = fcmStorage.fcmToken
        view.render(screenModel)
        logFcmToken()
    }

    fun copyFcmToken() {
        screenModel.fcmToken?.let {
            view.copyFcmToken()
            view.showMessage(stringsProvider.getString(R.string.fcm_copied_message))
            logFcmToken()
        }
    }

    private fun logFcmToken() {
        screenModel.fcmToken?.apply {
            if (isNotEmpty()) {
                Logger.d("FCM-token: $this")
            }
        }
    }
}
