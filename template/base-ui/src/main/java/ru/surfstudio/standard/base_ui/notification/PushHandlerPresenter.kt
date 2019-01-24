package ru.surfstudio.standard.base_ui.notification

import android.widget.Toast
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.standard.base_ui.navigation.PushHandlerActivityRoute
import javax.inject.Inject

@PerScreen
class PushHandlerPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val route: PushHandlerActivityRoute,
        private val pushClickHandler: PushClickHandler
) : BasePresenter<PushHandlerActivityView>(basePresenterDependency) {

    override fun onFirstLoad() {
        pushClickHandler.onPushClick(route.notification)
        finish()
    }
}