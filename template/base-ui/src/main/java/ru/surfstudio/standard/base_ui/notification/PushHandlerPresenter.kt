package ru.surfstudio.standard.base_ui.notification

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class PushHandlerPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val pushClickHandler: PushClickHandler
): BasePresenter<PushHandlerActivityView>(basePresenterDependency) {

    override fun onFirstLoad() {
        pushClickHandler.onPushClick(view.intent)
    }
}