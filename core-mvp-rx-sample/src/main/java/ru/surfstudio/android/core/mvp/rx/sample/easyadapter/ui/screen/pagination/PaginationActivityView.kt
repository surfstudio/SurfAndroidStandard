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

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.pagination_activity.*
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.domain.Element
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.animator.SlideItemAnimator
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.ElementController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.ElementStubController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.EmptyStateController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.ErrorStateController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.stub.Stub
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.utilktx.data.wrapper.selectable.SelectableData
import ru.surfstudio.sample.R
import javax.inject.Inject

/**
 * example screen with pagination
 * Placeholders is list items
 */
class PaginationActivityView : BaseRxActivityView<PaginationPresentationModel>() {

    @Inject
    lateinit var presenter: PaginationPresenter

    private val stubController = ElementStubController()
    private val emptyStateController = EmptyStateController()
    private lateinit var elementController: ElementController
    private lateinit var errorStateController: ErrorStateController

    private val adapter: PaginationableAdapter = PaginationableAdapter {}

    override fun getContentView(): Int = R.layout.pagination_activity

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> =
            PaginationScreenConfigurator(intent)

    override fun getPresenters() = arrayOf(presenter)

    override fun getScreenName(): String = "Pagination sample"

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        val linearLayoutManager = LinearLayoutManager(this)
        val itemAnimator = SlideItemAnimator()

        recycler.itemAnimator = itemAnimator
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = adapter
    }

    override fun bind(pm: PaginationPresentationModel) {
        adapter.setOnShowMoreListener { pm.getMoreAction.accept() }

        elementController = ElementController { pm.selectElementAction.accept(it) }
        errorStateController = ErrorStateController { pm.reloadAction.accept() }

        Observables.combineLatest(pm.loadState.observable,
                pm.elementsState.observable,
                pm.stubsState.observable,
                pm.paginationState.observable,
                ::createItemList) bindTo { p -> adapter.setItems(p.first, p.second) }
    }

    private fun createItemList(loadState: LS,
                               elements: DataList<SelectableData<Element>>,
                               stubs: List<Stub>,
                               paginationState: PaginationState): Pair<ItemList, PaginationState> {
        val itemList = when (loadState) {
            LS.LOADING -> ItemList.create(stubs, stubController)
            LS.ERROR -> ItemList.create(errorStateController)
            LS.EMPTY -> ItemList.create(emptyStateController)
            LS.NONE -> ItemList.create(elements, elementController)
        }
        return itemList to paginationState
    }

    fun showText(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}