package ru.surfstudio.android.broadcast.extension.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import ru.surfstudio.android.broadcast.extension.sample.R
import ru.surfstudio.android.broadcast.extension.sample.ui.base.configurator.ActivityScreenConfigurator
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

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }
}
