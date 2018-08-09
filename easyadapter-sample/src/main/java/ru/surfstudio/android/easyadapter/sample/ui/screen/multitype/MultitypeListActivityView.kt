package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.multitype_list_layout.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.domain.SecondData
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.EmptyItemController
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.FirstDataItemController
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.SecondDataItemController
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.TwoDataItemController
import javax.inject.Inject

class MultitypeListActivityView : BaseRenderableActivityView<MultitypeListScreenModel>() {

    @Inject
    lateinit var presenter: MultitypeListPresenter

    private val adapter = EasyAdapter()

    private val emptyItemController = EmptyItemController()
    private val twoDataItemController = TwoDataItemController()

    private val firstDataItemController = FirstDataItemController(
            object : FirstDataItemController.FirstDataClickListener {
                override fun onClick(firstData: FirstData) {
                    toast("Value = $firstData")
                }
            })

    private val secondDataItemController = SecondDataItemController(
            object : SecondDataItemController.SecondDataClickListener {
                override fun onClick(secondData: SecondData) {
                    toast("Value = ${secondData.stringValue}")
                }
            })

    override fun createConfigurator(): BaseActivityViewConfigurator<*, *, *> {
        return MultitypeListScreenConfigurator(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initRecycler()
    }

    override fun getContentView(): Int = R.layout.multitype_list_layout

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "Multitype List Activity"

    override fun renderInternal(screenModel: MultitypeListScreenModel) {
        adapter.setItems(ItemList.create()
                .add(emptyItemController)
                .add(screenModel.firstData, screenModel.secondData, twoDataItemController)
                .addAll(screenModel.firstDataList, firstDataItemController)
                .addAll(screenModel.secondDataList, secondDataItemController))
    }

    private fun initRecycler() {
        rvMultitypeList.layoutManager = LinearLayoutManager(this)
        rvMultitypeList.adapter = adapter
    }
}