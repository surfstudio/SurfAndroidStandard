package ru.surfstudio.standard.f_debug.info

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана показа общей информации
 */
@PerScreen
class InfoDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<InfoDebugActivityView>(basePresenterDependency) {
    private val screenModel = InfoDebugScreenModel()

    override fun onFirstLoad() {
        super.onFirstLoad()
        view.render(screenModel)
    }
}
