package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.interactor.FirstDataRepository
import javax.inject.Inject

@PerScreen
internal class PaginationListPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                  private val repository: FirstDataRepository
) : BasePresenter<PaginationListActivityView>(basePresenterDependency) {

    private val screenModel: PaginationListScreenModel = PaginationListScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            loadData()
        } else {
            view.render(screenModel)
        }
    }

    private fun loadData() {
        subscribe(getData()) {
            screenModel.list.addAll(it)
            view.render(screenModel)
        }
    }

    fun loadMore() = loadData()

    private fun getData(): Observable<List<FirstData>> {
        val result = repository.getData(screenModel.page++)
        screenModel.paginationState =
                when {
                    screenModel.page == FirstDataRepository.ERROR_PAGE_NUMBER -> PaginationState.ERROR
                    screenModel.page < FirstDataRepository.PAGES_COUNT -> PaginationState.READY
                    else -> PaginationState.COMPLETE
                }
        return result
    }
}