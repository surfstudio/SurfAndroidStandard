package ru.surfstudio.android.firebase.sample.ui.screen.push

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.NoDataNotificationTypeData
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import javax.inject.Inject

@PerScreen
internal class PushPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val pushInteractor: PushInteractor
) : BasePresenter<PushActivityView>(basePresenterDependency) {

    private val sm: PushScreenModel = PushScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)

        subscribe(pushInteractor.observeNotificationType(NoDataNotificationTypeData::class.java)) {
            Logger.i("GET PUSH FROM INTERACTOR ON $this")
        }
    }
}