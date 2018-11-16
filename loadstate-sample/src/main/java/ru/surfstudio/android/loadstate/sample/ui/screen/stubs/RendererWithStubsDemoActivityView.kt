package ru.surfstudio.android.loadstate.sample.ui.screen.stubs

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.loadstate.sample.R
import javax.inject.Inject

/**
 * Вью экрана todo
 */
class RendererWithStubsDemoActivityView : BaseRenderableActivityView<RendererWithStubsDemoScreenModel>() {

    @Inject
    lateinit var presenter: RendererWithStubsDemoPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = RendererWithStubsDemoScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_renderer_with_stubs_demo

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        findViews()
        initListeners()
    }

    override fun renderInternal(screenModel: RendererWithStubsDemoScreenModel) {
    }

    private fun findViews() {
    }

    private fun initListeners() {
    }

    override fun getScreenName(): String = "renderer_with_stubs_demo"
}
