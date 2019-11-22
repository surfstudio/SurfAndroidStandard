package ru.surfstudio.android.message.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*

import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.message.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = MainScreenConfigurator(intent)

    private fun initListeners() {
        show_colored_snackbar.setOnClickListener { presenter.showColoredSnackbar() }
        show_snackbar_with_duration.setOnClickListener { presenter.showSnackbarWithDuration() }
        show_snackbar_with_listener.setOnClickListener { presenter.showSnackbarWithListener() }
        show_gravity_toast.setOnClickListener { presenter.showGravityToast() }
        close_snackbar.setOnClickListener { presenter.closeSnackbar() }
        start_message_demo.setOnClickListener { presenter.startMessageDemo() }
    }
}
