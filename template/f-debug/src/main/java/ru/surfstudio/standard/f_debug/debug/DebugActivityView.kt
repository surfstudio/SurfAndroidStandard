package ru.surfstudio.standard.f_debug.debug

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.ActivityDebugBinding
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.DebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа информации для дебага
 */
class DebugActivityView : BaseRenderableActivityView<DebugScreenModel>() {

    private val binding by viewBinding(ActivityDebugBinding::bind) { rootView }

    @Inject
    lateinit var presenter: DebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = DebugScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_debug

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    override fun renderInternal(sm: DebugScreenModel) {}

    override fun getScreenName(): String = "debug"

    private fun initListeners() {
        with(binding){
            debugShowServerSettingsItemLayout.setOnClickListener { presenter.openServerSettingsScreen() }
            debugShowReusedComponentsItemLayout.setOnClickListener { presenter.openReusedComponentsScreen() }
            debugShowFcmTokenItemLayout.setOnClickListener { presenter.openFcmTokenScreen() }
            debugShowMemoryItemLayout.setOnClickListener { presenter.openMemoryScreen() }
            debugShowAppInfoItemLayout.setOnClickListener { presenter.openAppInfoScreen() }
            debugShowUiToolsItemLayout.setOnClickListener { presenter.openUiToolsScreen() }
            debugShowDeveloperToolsItemLayout.setOnClickListener { presenter.openDeveloperToolsScreen() }
            debugShowToolsItemLayout.setOnClickListener { presenter.openToolsScreen() }
            debugShowAppSettingsItemLayout.setOnClickListener { presenter.openAppSettingsScreen() }
        }
    }
}
