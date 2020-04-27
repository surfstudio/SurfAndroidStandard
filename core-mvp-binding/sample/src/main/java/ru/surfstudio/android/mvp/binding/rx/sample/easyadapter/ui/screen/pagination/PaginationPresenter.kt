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

package ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.pagination

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.pagination.PaginationState
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.interactor.element.DEFAULT_PAGE
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.interactor.element.ElementRepository
import ru.surfstudio.android.utilktx.data.wrapper.selectable.SelectableData
import ru.surfstudio.android.utilktx.data.wrapper.selectable.getSelectedDataNullable
import ru.surfstudio.android.utilktx.data.wrapper.selectable.setSelected
import javax.inject.Inject

@PerScreen
class PaginationPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val elementRepository: ElementRepository,
        private val bm: PaginationPresentationModel
) : BaseRxPresenter(basePresenterDependency) {

    private var loadMainSubscription: Disposable? = null
    private var loadMoreSubscription: Disposable? = null

    override fun onFirstLoad() {
        super.onFirstLoad()

        bm.reloadAction bindTo ::reloadData
        bm.getMoreAction.observable.filter { bm.hasData } bindTo ::loadMore
        bm.selectElementAction bindTo { element ->
            bm.elementsState.change {
                it.apply { setSelected(element) }
            }
        }

        loadMainData()
    }

    private fun loadMainData() {
        loadMainSubscription?.dispose()
        loadMoreSubscription?.dispose()

        loadMainSubscription = subscribeIo(
                elementRepository.getElements(DEFAULT_PAGE),
                { elements ->
                    bm.showMessageCommand.accept("Data loaded")
                    val newElements = updateDataList(elements)

                    bm.elementsState.accept(newElements)
                    bm.hasData = elements.isNotEmpty()
                    setNormalLoadState(elements)
                    setNormalPaginationState(elements)
                },
                {
                    setErrorLoadState(bm.elementsState.value)
                    setErrorPaginationState(bm.elementsState.value)
                    bm.showMessageCommand.accept("Imitate load data error")
                })
    }

    private fun loadMore() {
        bm.showMessageCommand.accept("Start pagination request")
        loadMoreSubscription =
                elementRepository.getElements(bm.elementsState.value.nextPage)
                        .observeOn(AndroidSchedulers.mainThread())
                        .bindTo(
                                { elements ->
                                    val newElements = updateDataList(elements)
                                    bm.elementsState.accept(bm.elementsState.value.merge(newElements))
                                    setNormalPaginationState(bm.elementsState.value)
                                    bm.showMessageCommand.accept("Pagination request complete")
                                },
                                {
                                    setErrorPaginationState(bm.elementsState.value)
                                    bm.showMessageCommand.accept("Imitate pagination request error")
                                })
    }

    private fun reloadData() {
        loadMainData()
    }

    /**
     *  Трансформирует DataList<Element>) в DataList<SelectableData<Element>> и устанавливает выбранное значение
     */
    private fun updateDataList(elements: DataList<Element>): DataList<SelectableData<Element>> {
        val selected = bm.elementsState.value.getSelectedDataNullable()
        return elements.map { SelectableData(it) }
                .let {
                    it.setSelected(selected)
                    DataList(it,
                            elements.startPage,
                            elements.pageSize,
                            elements.totalItemsCount,
                            elements.totalPagesCount)
                }
    }

    private fun setNormalLoadState(elements: DataList<*>) {
        bm.loadState.accept(if (elements.isEmpty()) Empty else None)
    }

    private fun setErrorLoadState(elements: DataList<*>) {
        bm.loadState.accept(if (elements.isEmpty())
            Error else None)
    }

    private fun setNormalPaginationState(elements: DataList<*>) {
        bm.paginationState.accept(if (elements.canGetMore())
            PaginationState.READY else
            PaginationState.COMPLETE)

    }

    private fun setErrorPaginationState(elements: DataList<*>) {
        bm.paginationState.accept(if (elements.isEmpty())
            PaginationState.COMPLETE else
            PaginationState.ERROR)
    }
}


