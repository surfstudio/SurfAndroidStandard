package ru.surfstudio.android.network.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.network.sample.interactor.product.ProductRepository
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val repository: ProductRepository
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    companion object {
        private const val FIRST_PAGE = 1
    }

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            tryLoadData()
        } else {
            view.render(screenModel)
        }
    }

    private fun tryLoadData(page: Int = FIRST_PAGE) {
        if (screenModel.loadState == LoadState.ERROR || screenModel.loadState == LoadState.EMPTY) {
            screenModel.loadState = LoadState.MAIN_LOADING
            view.render(screenModel)
        }
        loadData(page)
    }

    private fun loadData(page: Int) {
        subscribe(repository.getProducts(page), { productList ->
            screenModel.productList.merge(productList)
            screenModel.loadState = LoadState.NONE
            view.render(screenModel)
        }, {
            screenModel.loadState = LoadState.ERROR
            view.render(screenModel)
        })
    }

    fun reloadData() = tryLoadData(screenModel.productList.nextPage)

    fun loadMore() = loadData(screenModel.productList.nextPage)
}