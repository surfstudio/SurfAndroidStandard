package ru.surfstudio.android.network.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseLdsSwrActivityView
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.network.sample.R
import ru.surfstudio.android.network.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.network.sample.ui.screen.main.list.ProductItemController
import ru.surfstudio.android.network.sample.ui.screen.main.list.ProductListAdapter
import ru.surfstudio.android.sample.common.ui.base.loadstate.LoadState
import ru.surfstudio.android.sample.common.ui.base.loadstate.renderer.DefaultLoadStateRenderer
import ru.surfstudio.android.utilktx.ktx.ui.view.goneIf
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseLdsSwrActivityView<MainScreenModel>() {

    override fun getScreenName(): String = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @Inject
    internal lateinit var messageController: MessageController

    private val adapter = ProductListAdapter { presenter.loadMore() }

    private val productItemController = ProductItemController { messageController.show(it.name) }

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipe_refresh_layout

    override fun getLoadStateRenderer(): LoadStateRendererInterface =
            DefaultLoadStateRenderer(placeholder)
                    .configErrorState(onBtnClickedListener = { presenter.reloadData() })

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
        initRecycler()
    }

    override fun renderInternal(sm: MainScreenModel) {
        adapter.setItems(ItemList.create()
                .addAll(sm.productList, productItemController),
                sm.paginationState)
    }

    override fun renderLoadState(loadState: LoadStateInterface?) {
        super.renderLoadState(loadState)
        swipe_refresh_layout.goneIf(loadState != LoadState.NONE)
    }

    private fun initListeners() {
        swipe_refresh_layout.setOnRefreshListener { presenter.reloadData() }
    }

    private fun initRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}
