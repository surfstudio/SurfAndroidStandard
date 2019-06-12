package ru.surfstudio.android.mvp.binding.rx.sample.react

import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_reactive_list.*
import ru.surfstudio.android.core.mvp.binding.react.event.LoadNextData
import ru.surfstudio.android.core.mvp.binding.react.event.ReloadData
import ru.surfstudio.android.core.mvp.binding.react.ui.BaseReactActivity
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.ui.screen.pagination.PaginationableAdapter
import ru.surfstudio.android.mvp.binding.rx.sample.react.controller.BaseController
import ru.surfstudio.android.mvp.binding.rx.sample.react.di.ReactiveScreenConfigurator
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHubImpl
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.MainLoading
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.QueryChangedEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.ListFeature
import javax.inject.Inject

class ReactiveActivityView : BaseReactActivity() {

    private val adapter = PaginationableAdapter { sendEvent(LoadNextData()) }
    private val controller = BaseController()

    override fun createConfigurator() = ReactiveScreenConfigurator(intent)

    override fun getScreenName() = "ReactiveActivityView"

    override fun getContentView(): Int = R.layout.activity_reactive_list

    @Inject
    lateinit var listFeature: ListFeature

    @Inject
    override lateinit var hub: EventHubImpl

    override fun getFeatures() = arrayOf(listFeature)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        reactive_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        reactive_rv.adapter = adapter

        reactive_reload_btn.clicks().sendEvent(ReloadData())
        reactive_query_tv.textChanges()
                .skipInitialValue()
                .mapAndSend { QueryChangedEvent(it.toString()) }

        listFeature.state.observeLoading bindTo { reactive_pb.isVisible = it }
        listFeature.state.observeData bindTo ::createList
    }

    private fun createList(list: List<String>) {
        adapter.setItems(ItemList.create().addAll(list, controller), PaginationState.READY)
    }
}