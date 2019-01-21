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

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.pagination_activity.*
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.animator.SlideItemAnimator
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.ElementController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.ElementStubController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.EmptyStateController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.common.recycler.controller.ErrorStateController
import ru.surfstudio.android.core.mvp.rx.sample.easyadapter.ui.screen.main.list.HeaderController
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxActivityView
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.sample.R
import javax.inject.Inject

/**
 * example screen with pagination
 * Placeholders is list items
 */
class PaginationActivityView : BaseRxActivityView<PaginationScreenModel>() {

    @Inject
    lateinit var presenter: PaginationPresenter

    private lateinit var headerController: HeaderController
    private lateinit var elementController: ElementController
    private lateinit var stubController: ElementStubController
    private lateinit var emptyStateController: EmptyStateController
    private lateinit var errorStateController: ErrorStateController

    private val adapter: PaginationableAdapter = PaginationableAdapter {}


    override fun getContentView(): Int = R.layout.pagination_activity

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> =
            PaginationScreenConfigurator(intent)

    override fun getPresenters() = arrayOf(presenter)

    override fun getScreenName(): String = "Pagination sample"

    override fun bind(sm: PaginationScreenModel) {

        val linearLayoutManager = LinearLayoutManager(this)
        val itemAnimator = SlideItemAnimator()
        recycler.itemAnimator = itemAnimator
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = adapter
        adapter.setOnShowMoreListener { presenter.loadMore() }

        headerController = HeaderController()
        elementController = ElementController(
                onClickListener = { showText("on element ${it.name} click ") })
        stubController = ElementStubController()
        emptyStateController = EmptyStateController()
        errorStateController = ErrorStateController(
                onReloadClickListener = { presenter.reloadData() })
    }

    fun render(screenModel: PaginationScreenModel) {
        val itemList = when (screenModel.loadState) {
            LS.LOADING -> ItemList.create(screenModel.stubs, stubController)
            LS.ERROR -> ItemList.create(errorStateController)
            LS.EMPTY -> ItemList.create(emptyStateController)
            LS.NONE -> ItemList.create(screenModel.elements, elementController)
        }
        adapter.setItems(itemList, screenModel.paginationState)
    }

    fun showText(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}
