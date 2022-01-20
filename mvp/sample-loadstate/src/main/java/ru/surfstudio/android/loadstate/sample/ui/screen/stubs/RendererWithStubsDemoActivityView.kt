package ru.surfstudio.android.loadstate.sample.ui.screen.stubs

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_renderer_with_stubs_demo.*
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.LoadStateWithStubsRenderer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.ErrorLoadState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.NoneLoadState
import ru.surfstudio.android.loadstate.sample.ui.screen.ordinary.controllers.ExampleDataItemController
import javax.inject.Inject

/**
 * Вью экрана для демонстрации работы DefaultLoadStateRenderer с использованием заглушек (шиммеров)
 */
class RendererWithStubsDemoActivityView : BaseLdsActivityView<RendererWithStubsDemoScreenModel>() {

    private val adapter = EasyAdapter()

    private val exampleDataItemController = ExampleDataItemController()

    private val renderer: LoadStateWithStubsRenderer by lazy {
        LoadStateWithStubsRenderer(adapter)
            .apply {
                //пример управления видимостью View в зависимости от стейта
                setViewVisibleFor(
                    ErrorLoadState::class,
                    retry_btn
                )
            }
    }

    @Inject
    lateinit var presenter: RendererWithStubsDemoPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = RendererWithStubsDemoScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_renderer_with_stubs_demo

    override fun getScreenName(): String = "renderer_with_stubs_demo"

    override fun getLoadStateRenderer() = renderer

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        initViews()
        initListeners()
    }

    override fun renderInternal(screenModel: RendererWithStubsDemoScreenModel) {

        if (screenModel.loadState !is NoneLoadState) return

        adapter.setItems(
            ItemList.create()
                .addAll(screenModel.itemList, exampleDataItemController)
        )
    }

    private fun initViews() {
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun initListeners() {
        main_loading_btn.setOnClickListener { presenter.mainLoading() }
        error_btn.setOnClickListener { presenter.error() }
        none_btn.setOnClickListener { presenter.none() }
        retry_btn.setOnClickListener {
            Toast
                .makeText(this, R.string.retry_toast_msg, Toast.LENGTH_SHORT)
                .apply {
                    show()
                }
        }
    }
}
