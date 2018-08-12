package ru.surfstudio.android.network.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.support.v4.widget.SwipeRefreshLayout
import ru.surfstudio.android.core.mvp.activity.BaseLdsSwrActivityView
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.sample.ui.screen.pagination.ProductListAdapter
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.network.sample.R
import ru.surfstudio.android.network.sample.domain.product.Product
import ru.surfstudio.android.network.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.network.sample.ui.screen.main.list.ProductItemController
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseLdsSwrActivityView<MainScreenModel>() {

    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @Inject
    internal lateinit var messageController: MessageController

    private val adapter = ProductListAdapter { presenter.loadMore() }

    private val productItemController = ProductItemController(
            object : ProductItemController.OnProductClickListener {
                override fun onProductClick(product: Product)

                }
            })
    
    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): ActivityScreenConfigurator = MainScreenConfigurator(intent)

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipe_refresh_layout

    override fun getPlaceHolderView(): PlaceHolderViewInterface = placeholder

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
    }

    override fun renderInternal(screenModel: MainScreenModel) {

    }
}
