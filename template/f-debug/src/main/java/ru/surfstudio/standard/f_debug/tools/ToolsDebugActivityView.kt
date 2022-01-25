package ru.surfstudio.standard.f_debug.tools

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.ActivityToolsDebugBinding
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.ToolsDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа Tools
 */
class ToolsDebugActivityView : BaseRenderableActivityView<ToolsDebugScreenModel>() {

    private val binding by viewBinding(ActivityToolsDebugBinding::bind) { rootView }

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
        binding.debugStethoSwitch.setChecked(sm.isStethoEnabled)
    }

    private fun initListeners() {
        binding.debugStethoSwitch.setOnCheckedChangeListener { _, isEnable ->
            presenter.setStethoEnable(isEnable)
        }
    }
}
