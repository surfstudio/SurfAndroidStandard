package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.util.ClipboardManagerHelper
import ru.surfstudio.standard.i_debug.DebugInteractor
import javax.inject.Inject

/**
 * Презентер экрана показа fcm-токена
 */
@PerScreen
class DebugFcmPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                            private val stringsProvider: StringsProvider,
                                            private val debugInteractor: DebugInteractor,
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
        val fcmToken = debugInteractor.getFcmToken()
        screenModel.fcmToken = fcmToken
        screenModel.loadState = if (fcmToken.isEmpty()) LoadState.EMPTY else LoadState.NONE
        view.render(screenModel)
        Logger.d("FCM-token: $fcmToken")
    }

    fun copyFcmToken() {
        screenModel.fcmToken?.let {
            clipboardManagerHelper.copyString(it)
            view.showMessage(stringsProvider.getString(R.string.fcm_copied_message))
        }
    }
}
