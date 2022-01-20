package ru.surfstudio.standard.f_debug.memory

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.ActivityMemoryDebugBinding
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.MemoryDebugScreenConfigurator
import javax.inject.Inject

class MemoryDebugActivityView : BaseRenderableActivityView<MemoryDebugScreenModel>() {

    private val binding by viewBinding(ActivityMemoryDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: MemoryDebugPresenter

    override fun createConfigurator() = MemoryDebugScreenConfigurator(intent)

    override fun getScreenName() = "MemoryDebugActivityView"

    override fun getContentView() = R.layout.activity_memory_debug

    override fun getPresenters() = arrayOf(presenter)

    override fun renderInternal(model: MemoryDebugScreenModel) {
        binding.debugMemoryLeakcanarySwitch.setChecked(model.isLeakCanaryEnabled)
        initListeners()
    }

    private fun initListeners() {
        binding.debugMemoryLeakcanarySwitch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setLeakCanaryEnabled(isEnabled)
        }
        binding.debugShowStorageItemLayout.setOnClickListener { presenter.openStorageDebugScreen() }
    }
}