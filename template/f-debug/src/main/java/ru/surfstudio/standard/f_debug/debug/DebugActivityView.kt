package ru.surfstudio.standard.f_debug.debug

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import javax.inject.Inject

/**
 * Вью экрана показа информации для дебага
 */
class DebugActivityView : BaseRenderableActivityView<DebugScreenModel>() {

    @Inject
    lateinit var presenter: DebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

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
        show_server_settings_item_layout.setOnClickListener { presenter.openServerSettingsScreen() }
        show_controllers_item_layout.setOnClickListener { presenter.openControllersScreen() }
        show_fcm_token_item_layout.setOnClickListener { presenter.openFcmTokenScreen() }
        show_memory_item_layout.setOnClickListener { presenter.openMemoryScreen() }
    }
}
