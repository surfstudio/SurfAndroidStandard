package ru.surfstudio.android.loadstate.sample.ui.screen.actions

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана todo
 */
@PerScreen
class RendererWithActionsDemoPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<RendererWithActionsDemoActivityView>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }
}
