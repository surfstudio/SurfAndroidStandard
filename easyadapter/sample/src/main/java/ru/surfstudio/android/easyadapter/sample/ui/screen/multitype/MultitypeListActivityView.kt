package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.multitype_list_layout.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.sample.R
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.EmptyItemController
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.FirstDataItemController
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.SecondDataItemController
import ru.surfstudio.android.easyadapter.sample.ui.screen.common.controllers.TwoDataItemController
import javax.inject.Inject

class MultitypeListActivityView : BaseRenderableActivityView<MultitypeListScreenModel>() {

    @Inject
    internal lateinit var presenter: MultitypeListPresenter

    private val adapter = EasyAdapter()

    private val emptyItemController = EmptyItemController()
    private val twoDataItemController = TwoDataItemController()

    private val firstDataItemController = FirstDataItemController {
        Toast
            .makeText(this, "Value = $it", Toast.LENGTH_SHORT)
            .apply {
                show()
            }
    }

    private val secondDataItemController =
        SecondDataItemController {
            Toast
                .makeText(this, "Value = ${it.stringValue}", Toast.LENGTH_SHORT)
                .apply {
                    show()
                }
        }

    override fun createConfigurator(): CustomActivityScreenConfigurator {
        return MultitypeListScreenConfigurator(intent)
    }

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initRecycler()
    }

    override fun getContentView(): Int = R.layout.multitype_list_layout

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun getScreenName(): String = "Multitype List Activity"

    override fun renderInternal(sm: MultitypeListScreenModel) {
        adapter.setItems(
            ItemList.create()
                .addAll(sm.firstDataList, firstDataItemController)
                .addAll(sm.secondDataList, secondDataItemController)
                .add(emptyItemController)
                .add(sm.firstData, sm.secondData, twoDataItemController)
        )
    }

    private fun initRecycler() {
        rvMultitypeList.layoutManager = LinearLayoutManager(this)
        rvMultitypeList.adapter = adapter
    }
}