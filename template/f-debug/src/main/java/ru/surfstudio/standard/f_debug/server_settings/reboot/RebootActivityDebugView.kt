package ru.surfstudio.standard.f_debug.server_settings.reboot

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
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
    }

    override fun onBackPressed() {
        // отключаем возможность закрыть активность
    }
}