package ru.surfstudio.android.easyadapter.sample.ui.screen.async

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class AsyncInflateListPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<AsyncInflateListActivityView>(basePresenterDependency) {

    private val screenModel: AsyncInflateListScreenModel = AsyncInflateListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        screenModel.data = (0..100).toList().map(Int::toString)
        view.render(screenModel)
    }
}