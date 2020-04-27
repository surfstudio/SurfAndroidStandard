package ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.list

import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_reactive_list.*
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.pagination.PaginationState
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.list.controller.ComplexListController
import ru.surfstudio.android.core.mvi.sample.ui.adapter.PaginationableAdapter
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.observeMainLoading
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.observeSwrLoading
import javax.inject.Inject

/**
 * Экран, демонстрирующий сложный экран с загрузкой, пагинацией и фильтрацией списка
 */
class ComplexListActivityView : BaseReactActivityView() {

    private val adapter = PaginationableAdapter { ComplexListEvent.LoadNextPage.emit(hub) }
    private val controller = ComplexListController()

    override fun createConfigurator() = ComplexListScreenConfigurator(intent)

    override fun getScreenName() = "ComplexListActivityView"

    override fun getContentView(): Int = R.layout.activity_reactive_list

    @Inject
    lateinit var sh: ComplexListStateHolder

    @Inject
    lateinit var hub: ScreenEventHub<ComplexListEvent>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        reactive_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        reactive_rv.adapter = adapter

        reactive_reload_btn.clicks()
                .emit(ComplexListEvent.Reload, hub)

        reactive_query_tv.textChanges()
                .skipInitialValue()
                .map { ComplexListEvent.QueryChanged(it.toString()) }
                .emit(hub)

        reactive_swr.setOnRefreshListener { ComplexListEvent.SwipeRefresh.emit(hub) }

        sh.list.observeMainLoading() bindTo { reactive_pb.isVisible = it }
        sh.list.observeSwrLoading() bindTo { reactive_swr.isRefreshing = it }
        sh.filteredList bindTo ::createList
    }

    private fun createList(bundle: Pair<List<String>, PaginationState>) {
        val (list, state) = bundle
        adapter.setItems(ItemList.create().addAll(list, controller), state)
    }
}