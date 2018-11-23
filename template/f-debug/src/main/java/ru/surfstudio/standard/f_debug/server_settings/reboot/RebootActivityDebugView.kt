package ru.surfstudio.standard.f_debug.server_settings.reboot

import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.activity_reboot_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.RebootDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана перезапуска приложения
 */
class RebootActivityDebugView : BaseRenderableActivityView<RebootDebugScreenModel>() {

    @Inject
    lateinit var presenter: RebootDebugPresenter

    override fun getContentView() = R.layout.activity_reboot_debug

    override fun getScreenName() = "RebootActivityDebugView"

    override fun getPresenters() = arrayOf(presenter)

    override fun createConfigurator() = RebootDebugScreenConfigurator(intent)

    override fun renderInternal(sm: RebootDebugScreenModel) {
        reboot_tw.text = getString(R.string.reboot_tw_start_text, sm.secondBeforeReboot.toString())
    }

    override fun onBackPressed() {
        // отключаем возможность закрыть активность
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        initListeners()
    }

    private fun initListeners() {
        reboot_now_b.setOnClickListener { presenter.rebootNow() }
        reboot_cancel_b.setOnClickListener { presenter.cancelReboot() }
    }
}