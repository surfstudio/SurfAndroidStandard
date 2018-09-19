package ru.surfstudio.standard.f_debug.fcm

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import javax.inject.Inject

/**
 * Вью экрана показа fcm-токена
 */
class DebugFcmActivityView : BaseRenderableActivityView<DebugFcmScreenModel>() {

    @Inject
    lateinit var presenter: DebugFcmPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_debug_fcm

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        initListeners()
    }

    override fun renderInternal(screenModel: DebugFcmScreenModel) {
    }

    private fun initListeners() {
    }

    override fun getScreenName(): String = "debug_fcm"
}
