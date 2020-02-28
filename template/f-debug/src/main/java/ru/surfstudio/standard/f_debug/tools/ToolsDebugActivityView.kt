package ru.surfstudio.standard.f_debug.tools

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_tools_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.ToolsDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа Tools
 */
class ToolsDebugActivityView : BaseRenderableActivityView<ToolsDebugScreenModel>() {

    @Inject
    lateinit var presenter: ToolsDebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ToolsDebugScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_tools_debug

    override fun getScreenName(): String = "ToolsDebugActivityView"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initListeners()
    }

    override fun renderInternal(sm: ToolsDebugScreenModel) {
        debug_stetho_switch.setChecked(sm.isStethoEnabled)
    }

    private fun initListeners() {
        debug_stetho_switch.setOnCheckedChangeListener { _, isEnable ->
            presenter.setStethoEnable(isEnable)
        }
    }
}
