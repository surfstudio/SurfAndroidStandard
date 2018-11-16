package ru.surfstudio.android.loadstate.sample.ui.screen.ordinary

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_default_renderer_demo.*
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.CustomLoadStatePresentation
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.DefaultLoadStateRenderer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.CustomLoadState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.NoneLoadState
import ru.surfstudio.android.loadstate.sample.ui.screen.ordinary.controllers.ExampleDataItemController
import javax.inject.Inject

/**
 * Вью экрана todo
 */
class DefaultRendererDemoActivityView : BaseLdsActivityView<DefaultRendererDemoScreenModel>() {
    @Inject
    lateinit var presenter: DefaultRendererDemoPresenter

    private val exampleDataItemController = ExampleDataItemController()

    private val adapter = EasyAdapter()

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = DefaultRendererDemoScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_default_renderer_demo

    override fun createLoadStateRenderer() =
            DefaultLoadStateRenderer(placeholder).apply {
                //пример добавления представления кастомного стейта
                putPresentation(
                        CustomLoadState::class.java,
                        CustomLoadStatePresentation(placeholder))
                configEmptyState(onBtnClickedListener = { toast("WOW! Btn clicked") })
                configErrorState(onBtnClickedListener = { toast("WOW! Another btn clicked") })
            }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        initViews()
        initListeners()
    }

    override fun renderInternal(screenModel: DefaultRendererDemoScreenModel) {

        adapter.setItems(ItemList.create()
                .addAll(screenModel.itemList, exampleDataItemController))
    }

    private fun initViews() {
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun initListeners() {
        main_loading_btn.setOnClickListener { presenter.mainLoading() }
        tr_loading_btn.setOnClickListener { presenter.transparentLoading() }
        error_btn.setOnClickListener { presenter.error() }
        empty_btn.setOnClickListener { presenter.empty() }
        none_btn.setOnClickListener { presenter.none() }
        custom_btn.setOnClickListener { presenter.custom() }
    }

    override fun getScreenName(): String = "default_renderer_demo"
}
