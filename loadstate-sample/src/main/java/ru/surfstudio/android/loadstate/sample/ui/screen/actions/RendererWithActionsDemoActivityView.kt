package ru.surfstudio.android.loadstate.sample.ui.screen.actions

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана todo
 */
class RendererWithActionsDemoActivityView : BaseRenderableActivityView<RendererWithActionsDemoScreenModel>() {

    @Inject
    lateinit var presenter: RendererWithActionsDemoPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = RendererWithActionsDemoScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_renderer_with_actions_demo

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        findViews()
        initListeners()
    }

    override fun renderInternal(screenModel: RendererWithActionsDemoScreenModel) {
    }

    private fun findViews() {
    }

    private fun initListeners() {
    }

    override fun getScreenName(): String = "renderer_with_actions_demo"
}
