package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.all

import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_kitties_all.*
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.sample.R
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.all.KittiesAllEvent.Input
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.controller.HeaderItemController
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.controller.KittenItemController
import ru.surfstudio.android.core.mvi.ui.BaseReactActivityView
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import ru.surfstudio.android.sample.common.ui.base.easyadapter.PaginationFooterItemController
import javax.inject.Inject

internal class KittiesAllActivityView :
        BaseReactActivityView(),
        SingleHubOwner<KittiesAllEvent> {

    private val easyAdapter = EasyPaginationAdapter(PaginationFooterItemController()) {
        Input.LoadNext.emit()
    }

    private val kittenItemController = KittenItemController(
            onClickedAction = { /*no action*/ }
    )
    private val headerItemController = HeaderItemController(
            onBackClickedAction = { Input.BackClicked.emit() }
    )

    @Inject
    override lateinit var hub: ScreenEventHub<KittiesAllEvent>

    @Inject
    lateinit var sh: KittiesAllStateHolder

    override fun getScreenName(): String = "KittiesAllActivityView"

    override fun getContentView(): Int = R.layout.activity_kitties_all

    override fun createConfigurator() = KittiesAllScreenConfigurator(intent)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        initViews()
        bind()
    }

    private fun initViews() {
        kitties_all_rv.adapter = easyAdapter
        kitties_all_swr.setOnRefreshListener { Input.SwrReload.emit() }
    }

    private fun bind() {
        sh bindTo ::render
    }

    private fun render(state: KittiesAllState) {
        renderLoading(state)
        renderContent(state)
    }

    private fun renderLoading(state: KittiesAllState) {
        kitties_all_swr.isRefreshing = state.isSwr
        kitties_all_progress_bar.isVisible = state.isMainLoading || state.isTransparentLoading
        kitties_all_progress_bar.alpha = when {
            state.isMainLoading -> 1f
            else -> .33f
        }
    }

    private fun renderContent(state: KittiesAllState) {
        state.kittiesRequestUi.data?.safeGet { dataList, paginationState ->
            val itemList = ItemList.create()
                    .add(headerItemController)
                    .addAll(dataList, kittenItemController)

            easyAdapter.setItems(itemList, paginationState)
        }
    }
}