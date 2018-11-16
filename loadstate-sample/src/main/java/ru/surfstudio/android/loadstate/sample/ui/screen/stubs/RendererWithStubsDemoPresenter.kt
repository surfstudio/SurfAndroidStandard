package ru.surfstudio.android.loadstate.sample.ui.screen.stubs

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер экрана todo
 */
@PerScreen
class RendererWithStubsDemoPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<RendererWithStubsDemoActivityView>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }
}
