package ru.surfstudio.android.mvp.binding.rx.sample.react

import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_reactive_list.*
import ru.surfstudio.android.core.mvp.binding.react.ui.BaseReactActivityView
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.pagination.PaginationableAdapter
import ru.surfstudio.android.mvp.binding.rx.sample.react.controller.ReactiveListController
import ru.surfstudio.android.mvp.binding.rx.sample.react.di.ReactiveListScreenConfigurator
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHubImpl
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ReactiveList
import ru.surfstudio.android.mvp.binding.rx.sample.react.extension.observeMainLoading
import ru.surfstudio.android.mvp.binding.rx.sample.react.extension.observeSwrLoading
import ru.surfstudio.android.mvp.binding.rx.sample.react.reactor.ReactiveListStateHolder
import javax.inject.Inject

class ReactiveListActivityView : BaseReactActivityView() {

    private val adapter = PaginationableAdapter { ReactiveList.LoadNextPage().sendTo(hub) }
    private val controller = ReactiveListController()

    override fun createConfigurator() = ReactiveListScreenConfigurator(intent)

    override fun getScreenName() = "ReactiveListActivityView"

    override fun getContentView(): Int = R.layout.activity_reactive_list

    @Inject
    lateinit var bm: ReactiveListStateHolder

    @Inject
    lateinit var hub: EventHubImpl<ReactiveList>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        reactive_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        reactive_rv.adapter = adapter

        reactive_reload_btn.clicks()
                .map { ReactiveList.Reload() }
                .sendTo(hub)

        reactive_query_tv.textChanges()
                .skipInitialValue()
                .map { ReactiveList.QueryChanged(it.toString()) }
                .sendTo(hub)

        reactive_swr.setOnRefreshListener { ReactiveList.SwipeRefresh().sendTo(hub) }

        bm.state.observeMainLoading() bindTo { reactive_pb.isVisible = it }
        bm.state.observeSwrLoading() bindTo { reactive_swr.isRefreshing = it }
        bm.state.observeData bindTo ::createList
    }

    private fun createList(list: List<String>) {
        adapter.setItems(ItemList.create().addAll(list, controller), PaginationState.READY)
    }
}