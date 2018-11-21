package ru.surfstudio.standard.f_debug.server_settings

import kotlinx.android.synthetic.main.activity_server_settings_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.ServerSettingsDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана настроек сервера
 */
class ServerSettingsDebugActivityView : BaseRenderableActivityView<ServerSettingsDebugScreenModel>() {

    @Inject
    lateinit var presenter: ServerSettingsDebugPresenter

    override fun getContentView() = R.layout.activity_server_settings_debug

    override fun getScreenName() = "ServerSettingsDebugActivityView"

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator() = ServerSettingsDebugScreenConfigurator(intent)

    override fun renderInternal(sm: ServerSettingsDebugScreenModel) {
        server_settings_chuck_switch.setChecked(sm.isChuckEnabled)
        addCheckedChangeListener()
    }

    private fun addCheckedChangeListener() {
        server_settings_chuck_switch.setOnCheckedChangeListener { _, _ ->
            presenter.setChuckEnabled(server_settings_chuck_switch.isChecked())
        }
    }
}