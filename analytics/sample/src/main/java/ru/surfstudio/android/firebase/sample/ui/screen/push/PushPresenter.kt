package ru.surfstudio.android.firebase.sample.ui.screen.push

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.ui.common.notification.strategies.type.NotificationTypeData
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.PushNotificationsListener
import javax.inject.Inject

@PerScreen
internal class PushPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val pushNotificationsListener: PushNotificationsListener
) : BasePresenter<PushActivityView>(basePresenterDependency) {

    private val sm: PushScreenModel = PushScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)

        subscribe(
                pushNotificationsListener.observePushNotificationsByType(NotificationTypeData::class.java)
        ) {
            Logger.i("GET PUSH FROM INTERACTOR ON $this")
        }
    }
}