package ru.surfstudio.android.network.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.network.sample.interactor.product.ProductRepository
import ru.surfstudio.android.sample.common.ui.base.loadstate.LoadState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val repository: ProductRepository
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    companion object {
        private const val FIRST_PAGE = 1
    }

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            tryLoadData()
        } else {
            view.render(sm)
        }
    }

    private fun tryLoadData(page: Int = FIRST_PAGE) {
        if (sm.loadState == LoadState.ERROR || sm.loadState == LoadState.EMPTY) {
            sm.loadState = LoadState.MAIN_LOADING
            view.render(sm)
        }
        loadData(page)
    }

    private fun loadData(page: Int) {
        subscribeIoHandleError(repository.getProducts(page)
                .timeout(1000L, TimeUnit.MILLISECONDS), { productList ->
            sm.productList.merge(productList)
            sm.loadState = LoadState.NONE
            sm.setNormalPaginationState(productList.canGetMore())

            view.render(sm)
        }, {
            sm.loadState =
                    if (sm.hasContent())
                        LoadState.NONE
                    else
                        LoadState.ERROR
            sm.setErrorPaginationState()

            view.render(sm)
        })
    }

    fun reloadData() = tryLoadData(sm.productList.nextPage)

    fun loadMore() = loadData(sm.productList.nextPage)
}