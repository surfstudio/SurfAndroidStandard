package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import android.os.Bundle
import android.os.PersistableBundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.paginationable_list_layout.*
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.FirstDataItemController
import javax.inject.Inject

class PaginationListActivityView : BaseRenderableActivityView<PaginationListScreenModel>() {

    @Inject
    internal lateinit var presenter: PaginationListPresenter

    private val adapter = PaginationableAdapter { presenter.loadMore() }

    private val controller = FirstDataItemController { toast(it.toString()) }

    override fun createConfigurator(): CustomActivityScreenConfigurator {
        return PaginationListScreenConfigurator(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initRecycler()
    }

    override fun getContentView(): Int = R.layout.paginationable_list_layout

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "Pagination List Activity"

    override fun renderInternal(sm: PaginationListScreenModel) {
        adapter.setItems(ItemList.create()
                .addAll(sm.pageList, controller), sm.paginationState)
    }

    private fun initRecycler() {
        rvPaginationableList.layoutManager = LinearLayoutManager(this)
        rvPaginationableList.adapter = adapter
    }
}