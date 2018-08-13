package ru.surfstudio.android.analytics.sample.ui.screen.main

import android.support.annotation.IdRes
import ru.surfstudio.android.analytics.sample.R
import ru.surfstudio.android.analytics.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }
}
