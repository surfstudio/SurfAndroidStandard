package ru.surfstudio.standard.ui.screen.main

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.app.interactor.common.DataPriority
import ru.surfstudio.android.core.domain.Unit
import ru.surfstudio.android.core.domain.datalist.DataList
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.util.PaginationableUtil
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()
    private val repository = ru.surfstudio.standard.ui.screen.main.SampleRepository()

    private var loadMainDisposable: Disposable? = null
    private var loadPortionDisposable: Disposable? = null

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if(viewRecreated) {
            view.render(screenModel)
        } else {
            showLoading()
            loadData(getDataObservable(DataPriority.SERVER))
        }
    }

    private fun showLoading(minimalLoadingState: LoadState) {
        screenModel.showLoading(minimalLoadingState)
        view.render(screenModel)
    }

    private fun showLoading() {
        showLoading(LoadState.SMALL_LOADING)
    }


    private fun loadData(dataObservable : Observable<DataList<Unit>>) {
        loadMainDisposable = subscribeIoHandleErrorAutoReload<DataList<Unit>>(
                dataObservable,
                {
                    showLoading()
                    reloadData()
                },
                { newData : DataList<Unit> ->
                    screenModel.replaceData(newData)
                    screenModel.hideLoadingNormal()
                    view.render(screenModel)
                },
                { e ->
                    screenModel.hideLoadingError()
                    view.render(screenModel)
                })
    }

    private fun reloadData() {
        loadData(getPortionsDataObservable())
    }

    fun loadMore() {
        loadPortionDisposable = subscribeIoHandleError<DataList<Unit>>(
                repository.getData(DataPriority.SERVER, screenModel.list.nextSkip),
                { newBlock : DataList<Unit> ->
                    screenModel.list.merge(newBlock)
                    screenModel.setNormalPaginationState(screenModel.list.canGetMore()) //todo hide parameter inside method
                    view.render(screenModel)
                },
                { e ->
                    screenModel.setErrorPaginationState()
                    view.render(screenModel)
                })
    }

    private fun getDataObservable(dataPriority: DataPriority): Observable<DataList<Unit>> {
        return repository.getData(dataPriority, 0)
    }

    private fun getPortionsDataObservable(): Observable<DataList<Unit>> {
        return PaginationableUtil.getPaginationRequestPortions(
                {skip -> repository.getData(DataPriority.ONLY_ACTUAL, skip)},
                screenModel.list.skip)//todo зависит от механизма пагинации
    }


}

class SampleRepository {

    fun getData(dataPriority: DataPriority, skip: Int
    ) : Observable<DataList<Unit>> {
        return Observable.empty() //pass default count
    }
}