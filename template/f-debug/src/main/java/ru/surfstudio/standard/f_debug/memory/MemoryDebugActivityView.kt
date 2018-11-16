package ru.surfstudio.standard.f_debug.memory

import kotlinx.android.synthetic.main.activity_memory_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import javax.inject.Inject

class MemoryDebugActivityView : BaseRenderableActivityView<MemoryDebugScreenModel>() {

    @Inject
    lateinit var presenter: MemoryDebugPresenter

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    override fun getScreenName() = "MemoryDebugActivityView"

    override fun getContentView() = R.layout.activity_memory_debug

    override fun getPresenters() = arrayOf(presenter)

    override fun renderInternal(model: MemoryDebugScreenModel) {
        memory_leakcanary_switch.setChecked(model.isLeakCanaryEnabled)
        initListeners()
    }

    private fun initListeners() {
        memory_leakcanary_switch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setLeakCanaryEnabled(isEnabled)
        }
    }
}