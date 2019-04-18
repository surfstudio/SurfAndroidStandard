package ru.surfstudio.standard.f_debug.ui_tools

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_ui_tools_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.UiToolsDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа UI-tools
 */
class UiToolsDebugActivityView : BaseRenderableActivityView<UiToolsDebugScreenModel>() {

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
        fps_enable_switch.setChecked(sm.isFpsEnabled)
    }

    private fun initListeners() {
        fps_enable_switch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setFpsEnable(isEnabled)
        }
        scalpel_tool.setOnClickListener {
            Toast.makeText(this, "Втряхните устройство для включения Scalpel", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}
