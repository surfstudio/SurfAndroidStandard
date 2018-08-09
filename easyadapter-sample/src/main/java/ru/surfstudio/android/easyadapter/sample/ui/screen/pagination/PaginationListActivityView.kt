package ru.surfstudio.android.easyadapter.sample.ui.screen.pagination

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.sample.R
import javax.inject.Inject

class PaginationListActivityView : BaseRenderableActivityView<PaginationListScreenModel>() {

    @Inject
    lateinit var presenter: PaginationListPresenter

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> {
        return PaginationListScreenConfigurator(intent)
    }

    override fun getContentView(): Int = R.layout.paginationable_list_layout

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "Pagination List Activity"

    override fun renderInternal(screenModel: PaginationListScreenModel?) {

    }
}