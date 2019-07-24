package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
internal class AsyncDiffPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<AsyncDiffActivityView>(basePresenterDependency) {

    private val screenModel: AsyncDiffScreenModel = AsyncDiffScreenModel()

    override fun onFirstLoad() {
        screenModel.generateNewDataList()
    }

    override fun onLoad(viewRecreated: Boolean) {
        render()
    }

    fun generateNewDataList() {
        screenModel.generateNewDataList()
        render()
    }

    private fun render() = view?.render(screenModel)
}