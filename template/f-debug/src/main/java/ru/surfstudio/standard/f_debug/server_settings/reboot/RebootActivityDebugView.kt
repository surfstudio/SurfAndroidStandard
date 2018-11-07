package ru.surfstudio.standard.f_debug.server_settings.reboot

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_reboot_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import ru.surfstudio.standard.f_debug.server_settings.ServerSettingsDebugActivityView
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

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    override fun renderInternal(sm: RebootDebugScreenModel) {
        reboot_tw.text = String.format(getString(R.string.reboot_tw_countdown_text), sm.countdownToRestart)
        if (sm.countdownToRestart == 0L) {
            shutdown()
        }
    }

    private fun shutdown() {
        val startActivity = Intent(this, ServerSettingsDebugActivityView::class.java)
        val pendingIntentId = 123456
        val pendingIntent = PendingIntent.getActivity(
                this,
                pendingIntentId,
                startActivity,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
        System.exit(0)
    }

    override fun onResume() {
        super.onResume()
        presenter.rebootApp()
    }
}