package ru.surfstudio.standard.f_debug.ui_tools

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.ActivityUiToolsDebugBinding
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.UiToolsDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа UI-tools
 */
class UiToolsDebugActivityView : BaseRenderableActivityView<UiToolsDebugScreenModel>() {

    private val binding by viewBinding(ActivityUiToolsDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: UiToolsDebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = UiToolsDebugScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_ui_tools_debug

    override fun getScreenName(): String = "UiToolsDebugActivityView"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initListeners()
    }

    override fun renderInternal(sm: UiToolsDebugScreenModel) {
        binding.debugFpsEnableSwitch.setChecked(sm.isFpsEnabled)
    }

    private fun initListeners() {
        with(binding) {
            debugFpsEnableSwitch.setOnCheckedChangeListener { _, isEnabled ->
                presenter.setFpsEnable(isEnabled)
            }
            debugScalpelTool.setOnClickListener {
                Toast.makeText(this@UiToolsDebugActivityView,
                        "Втряхните устройство для включения Scalpel",
                        Toast.LENGTH_SHORT)
                        .show()
            }
            debugVqaTool.setOnClickListener { presenter.openWindowVQA() }
        }

    }
}
