package ru.surfstudio.standard.f_debug.server_settings

import android.os.Bundle
import android.os.PersistableBundle
import com.jakewharton.rxbinding2.widget.RxSeekBar
import kotlinx.android.synthetic.main.activity_server_settings_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.template.f_debug.R
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
        debug_server_settings_chuck_switch.setChecked(sm.isChuckEnabled)
        debug_server_settings_test_server_switch.setChecked(sm.isTestServerEnabled)
        debug_server_settings_request_delay_tv.text = getString(R.string.debug_server_settings_request_delay_text, sm.requestDelaySeconds)
        debug_server_settings_request_delay_seek_bar.progress = sm.requestDelayCoefficient
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    private fun initListeners() {
        debug_server_settings_chuck_switch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setChuckEnabled(isEnabled)
        }
        debug_server_settings_test_server_switch.setOnCheckedChangeListener { _, isEnabled ->
            presenter.setTestServerEnabled(isEnabled)
        }
        presenter.requestDelayCoefficientChanges(RxSeekBar.userChanges(debug_server_settings_request_delay_seek_bar).skipInitialValue())
    }
}