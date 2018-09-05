package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.paginationable_list_layout.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.FirstDataItemController
import javax.inject.Inject

class PaginationListActivityView : BaseRenderableActivityView<PaginationListScreenModel>() {

    @Inject
    internal lateinit var presenter: PaginationListPresenter

    private val adapter = PaginationableAdapter { presenter.loadMore() }

    private val controller = FirstDataItemController(object : FirstDataItemController.FirstDataClickListener {
        override fun onClick(firstData: FirstData) {
            toast(firstData.toString())
        }
    })

    override fun createConfigurator(): ActivityScreenConfigurator {
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

    override fun renderInternal(screenModel: PaginationListScreenModel) {
        adapter.setItems(ItemList.create()
                .addAll(screenModel.pageList, controller), screenModel.paginationState)
    }

    private fun initRecycler() {
        rvPaginationableList.layoutManager = LinearLayoutManager(this)
        rvPaginationableList.adapter = adapter
    }
}