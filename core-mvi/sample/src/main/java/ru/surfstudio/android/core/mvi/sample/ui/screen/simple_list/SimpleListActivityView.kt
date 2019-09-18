package ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list

import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_simple_list.*
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.BaseEventHub
import ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list.controller.StepperButtonController
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import javax.inject.Inject

/**
 * Экран с простым списком без загрузки и пагинации.
 * Демонстрирует простейший механизм flatMap Middleware
 */
class SimpleListActivityView : BaseRxActivityView(), SingleHubOwner<SimpleListEvent> {

    @Inject
    override lateinit var hub: BaseEventHub<SimpleListEvent>

    @Inject
    lateinit var sh: SimpleListStateHolder

    val adapter = EasyAdapter()
    val controller = StepperButtonController { SimpleListEvent.StepperClicked(it).emit() }

    override fun createConfigurator() = SimpleListScreenConfigurator(intent)

    override fun getScreenName(): String = "SimpleListActivityView"

    override fun getContentView(): Int = R.layout.activity_simple_list

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        initViews()
        sh.items bindTo ::createItemList
    }

    private fun initViews() {
        simple_list_rv.adapter = adapter
    }

    private fun createItemList(list: List<Int>) {
        adapter.setItems(ItemList.create(list, controller))
    }
}