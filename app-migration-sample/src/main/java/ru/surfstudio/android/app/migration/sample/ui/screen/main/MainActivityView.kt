package ru.surfstudio.android.app.migration.sample.ui.screen.main

import androidx.annotation.IdRes
import ru.surfstudio.android.app.migration.sample.R
import ru.surfstudio.android.app.migration.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)

    override fun getScreenName(): String  = "MainActivity"
}
