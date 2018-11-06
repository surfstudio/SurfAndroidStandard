package ru.surfstudio.standard.f_debug.server_settings

import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_server_settings_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
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

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    override fun renderInternal(sm: ServerSettingsDebugScreenModel) {
    }

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    private fun initListeners() {
        server_settings_chuck_switch.setOnCheckedChangeListener { _, _ ->
            // While do nothing.
        }
    }
}