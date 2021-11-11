package ru.surfstudio.android.loadstate.sample.ui.screen.ordinary

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_default_renderer_demo.*
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.CustomLoadStatePresentation
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.DefaultLoadStateRenderer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.CustomLoadState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.ErrorLoadState
import ru.surfstudio.android.loadstate.sample.ui.screen.ordinary.controllers.ExampleDataItemController
import javax.inject.Inject

/**
 * Вью экрана для демонстрации работы DefaultLoadStateRenderer
 */
class DefaultRendererDemoActivityView : BaseLdsActivityView<DefaultRendererDemoScreenModel>() {
    @Inject
    lateinit var presenter: DefaultRendererDemoPresenter

    private val exampleDataItemController = ExampleDataItemController()

    private val adapter = EasyAdapter()

    private val renderer: DefaultLoadStateRenderer by lazy {
        DefaultLoadStateRenderer(placeholder)
            .apply {
                //пример добавления представления кастомного стейта
                putPresentation(
                    CustomLoadState::class,
                    CustomLoadStatePresentation(placeholder)
                )

                // установка листнеров на кнопки, при необходимости смена ресурсов
                configEmptyState(onBtnClickedListener = {
                    Toast
                        .makeText(
                            this@DefaultRendererDemoActivityView,
                            R.string.empty_state_toast_msg,
                            Toast.LENGTH_SHORT
                        )
                        .apply {
                            show()
                        }
                })
                configErrorState(onBtnClickedListener = {
                    Toast
                        .makeText(
                            this@DefaultRendererDemoActivityView,
                            R.string.error_state_toast_msg,
                            Toast.LENGTH_SHORT
                        )
                        .apply {
                            show()
                        }
                })

                //пример задания дополнительных действий при смене лоадстейта
                forState(ErrorLoadState::class,
                    run = { colorToolbar(R.color.colorAccent) },
                    elseRun = { colorToolbar(R.color.colorPrimary) })
            }
    }

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = DefaultRendererDemoScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_default_renderer_demo

    override fun getScreenName(): String = "default_renderer_demo"

    override fun getLoadStateRenderer() = renderer

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        initViews()
        initListeners()
    }

    override fun renderInternal(screenModel: DefaultRendererDemoScreenModel) {
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
        tr_loading_btn.setOnClickListener { presenter.transparentLoading() }
        error_btn.setOnClickListener { presenter.error() }
        empty_btn.setOnClickListener { presenter.empty() }
        none_btn.setOnClickListener { presenter.none() }
        custom_btn.setOnClickListener { presenter.custom() }
    }

    private fun colorToolbar(@ColorRes color: Int) {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, color)))
    }
}
