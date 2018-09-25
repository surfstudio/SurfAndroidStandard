package ru.surfstudio.standard.f_debug.debug_controllers

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import javax.inject.Inject

/**
 * Вью экрана для показа контроллеров, используемых в приложении
 */
class DebugControllersActivityView : BaseRenderableActivityView<DebugControllersScreenModel>() {

    @Inject
    lateinit var presenter: DebugControllersPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_debug_controllers

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        initListeners()
    }

    override fun renderInternal(screenModel: DebugControllersScreenModel) {
    }

    private fun initListeners() {
    }

    override fun getScreenName(): String = "debug_controllers"
}
