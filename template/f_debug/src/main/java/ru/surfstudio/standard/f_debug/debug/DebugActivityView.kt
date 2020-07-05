package ru.surfstudio.standard.f_debug.debug

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.DebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа информации для дебага
 */
class DebugActivityView : BaseRenderableActivityView<DebugScreenModel>() {

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
        debug_show_server_settings_item_layout.setOnClickListener { presenter.openServerSettingsScreen() }
        debug_show_reused_components_item_layout.setOnClickListener { presenter.openReusedComponentsScreen() }
        debug_show_fcm_token_item_layout.setOnClickListener { presenter.openFcmTokenScreen() }
        debug_show_memory_item_layout.setOnClickListener { presenter.openMemoryScreen() }
        debug_show_app_info_item_layout.setOnClickListener { presenter.openAppInfoScreen() }
        debug_show_ui_tools_item_layout.setOnClickListener { presenter.openUiToolsScreen() }
        debug_show_developer_tools_item_layout.setOnClickListener { presenter.openDeveloperToolsScreen() }
        debug_show_tools_item_layout.setOnClickListener { presenter.openToolsScreen() }
        debug_show_app_settings_item_layout.setOnClickListener { presenter.openAppSettingsScreen() }
    }
}
