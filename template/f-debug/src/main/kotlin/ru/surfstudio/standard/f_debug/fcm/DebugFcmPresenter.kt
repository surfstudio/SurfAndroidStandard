package ru.surfstudio.standard.f_debug.fcm

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.i_debug.DebugInteractor
import javax.inject.Inject

/**
 * Презентер экрана показа fcm-токена
 */
@PerScreen
class DebugFcmPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                            private val debugInteractor: DebugInteractor
) : BasePresenter<DebugFcmActivityView>(basePresenterDependency) {

    private val screenModel = DebugFcmScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun loadFcmToken() {
        val fcmToken = debugInteractor.getFcmToken()
        view.showFcmToken(fcmToken)
        Logger.d("FCM-token: $fcmToken")
    }
}
