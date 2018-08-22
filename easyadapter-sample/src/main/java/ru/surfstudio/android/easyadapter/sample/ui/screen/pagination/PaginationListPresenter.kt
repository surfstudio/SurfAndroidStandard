package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.interactor.DataListLimitOffset
import ru.surfstudio.android.easyadapter.sample.interactor.DataListPageCount
import ru.surfstudio.android.easyadapter.sample.interactor.FirstDataRepository
import java.util.concurrent.TimeUnit
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
        subscribeIoHandleError(getDataByPage()
                .delay(getDelay(), TimeUnit.MILLISECONDS)
                .timeout(1000L, TimeUnit.MILLISECONDS), //в целях демонстрации
                {
                    with(screenModel) {
                        pageList.merge(it)
                        screenModel.setNormalPaginationState(pageList.canGetMore())
                    }

                    view.render(screenModel)
                },
                {
                    screenModel.setErrorPaginationState()
                    view.render(screenModel)
                })
    }

    //демонстрация ошибки
    private fun getDelay() =
            if (screenModel.pageList.nextPage > FirstDataRepository.ERROR_PAGE_NUMBER) {
                Long.MAX_VALUE
            } else {
                500L
            }

    fun loadMore() = loadData()

    /**
     * Вариант загрузки по номеру страницы
     */
    private fun getDataByPage(): Observable<DataList<FirstData>> {
        return repository.getDataByPage(screenModel.pageList.nextPage)
    }

}