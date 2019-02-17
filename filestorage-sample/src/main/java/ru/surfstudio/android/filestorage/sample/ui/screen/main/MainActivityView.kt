package ru.surfstudio.android.filestorage.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseLdsSwrActivityView
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.filestorage.sample.R
import ru.surfstudio.android.filestorage.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.sample.common.ui.base.loadstate.LoadState
import ru.surfstudio.android.utilktx.ktx.ui.view.goneIf
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseLdsSwrActivityView<MainScreenModel>() {

    @Inject
    internal lateinit var presenter: MainPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = swipe_refresh_layout

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    override fun getLoadStateRenderer(): LoadStateRendererInterface = LoadStateRendererInterface {
        //empty
    }

    override fun getScreenName(): String = "MainActivity"

    override fun renderLoadState(loadState: LoadStateInterface?) {
        super.renderLoadState(loadState)
        swipe_refresh_layout.goneIf(loadState != LoadState.NONE)
    }

    override fun renderInternal(screenModel: MainScreenModel) {
        ip_tv.text = screenModel.ip?.value
        loadStateRenderer.render(screenModel.loadState)
    }

    private fun initListeners() {
        swipe_refresh_layout.setOnRefreshListener { presenter.reloadData() }

        save_to_serializable_cache_btn.setOnClickListener { presenter.saveIpToSerializableCache() }
        save_to_json_cache_btn.setOnClickListener { presenter.saveIpToJsonCache() }

        get_from_serializable_cache_btn.setOnClickListener { presenter.loadDataFromSerializableCache() }
        get_from_json_cache_btn.setOnClickListener { presenter.loadDataFromJsonCache() }

        clear_cache_btn.setOnClickListener { presenter.clearCache() }
    }
}
