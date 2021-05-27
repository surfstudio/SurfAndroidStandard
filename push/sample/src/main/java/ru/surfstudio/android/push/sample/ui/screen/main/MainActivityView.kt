package ru.surfstudio.android.push.sample.ui.screen.main

import androidx.annotation.IdRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.push.sample.R
import ru.surfstudio.android.push.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>(), PushHandlingActivity {
    override fun getScreenName(): String = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)
}