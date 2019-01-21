/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.pagination

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.interactor.element.DEFAULT_PAGE
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.interactor.element.ElementRepository
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class PaginationPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        val elementRepository: ElementRepository
) : BaseRxPresenter<PaginationScreenModel, PaginationActivityView>(basePresenterDependency) {

    override fun getRxModel(): PaginationScreenModel = PaginationScreenModel(LS.LOADING)

    private val screenModel = PaginationScreenModel(LS.LOADING)
    private var loadMainSubscription: Disposable? = null
    private var loadMoreSubscription: Disposable? = null

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (viewRecreated) {
            view.render(screenModel)
        } else {
            loadMainData()
        }
    }

    private fun loadMainData() {
        loadMainSubscription?.dispose()
        loadMoreSubscription?.dispose()
        if (!screenModel.hasData()) {
            screenModel.loadState = LS.LOADING
            view.render(screenModel)
        }
        loadMainSubscription = subscribe(
                elementRepository.getElements(DEFAULT_PAGE)
                        .observeOn(AndroidSchedulers.mainThread()),
                { elements ->
                    screenModel.elements = elements
                    screenModel.setNormalLoadState()
                    screenModel.setNormalPaginationState()
                    view.render(screenModel)
                },
                { _ ->
                    screenModel.setErrorLoadState()
                    screenModel.setErrorPaginationState()
                    view.showText("Imitate load data error")
                    view.render(screenModel)
                })
    }

    fun loadMore() {
        view.showText("Start pagination request")
        loadMoreSubscription = subscribe(
                elementRepository.getElements(screenModel.elements.nextPage)
                        .observeOn(AndroidSchedulers.mainThread()),
                { elements ->
                    screenModel.elements.merge(elements)
                    screenModel.setNormalPaginationState()
                    view.showText("Pagination request complete")
                    view.render(screenModel)
                },
                { _ ->
                    screenModel.setErrorPaginationState()
                    view.showText("Imitate pagination request error")
                    view.render(screenModel)
                })
    }

    fun reloadData() {
        loadMainData()
    }

}


