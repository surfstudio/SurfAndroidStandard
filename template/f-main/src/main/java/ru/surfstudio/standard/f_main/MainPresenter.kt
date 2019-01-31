package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.base_ui.notification.PushClickHandler
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val pushClickHandler: PushClickHandler
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
        subscribe(pushClickHandler.pushClickObservable)
        {
            Logger.d("Push received")
        }
    }
}
