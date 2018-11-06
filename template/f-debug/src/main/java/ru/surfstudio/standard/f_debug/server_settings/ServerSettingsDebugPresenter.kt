package ru.surfstudio.standard.f_debug.server_settings

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана настроек сервера
 */
@PerScreen
class ServerSettingsDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<ServerSettingsDebugActivityView>(basePresenterDependency) {

    private val sm: ServerSettingsDebugScreenModel = ServerSettingsDebugScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }
}