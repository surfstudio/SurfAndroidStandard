package ru.surfstudio.standard.ui.screen.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes

import javax.inject.Inject

import ru.surfstudio.android.core.ui.base.screen.activity.BaseRenderableHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.base.configurator.ActivityConfigurator

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableHandleableErrorActivityView<MainScreenModel>() {
    @set:Inject
    private var presenter: MainPresenter? = null

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter as CorePresenter<*>)
    }

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun createActivityConfigurator(): BaseActivityConfigurator<*, *> {
        return ActivityConfigurator(this)
    }

    override fun createScreenConfigurator(activity: Activity, intent: Intent): ScreenConfigurator<*> {
        return MainScreenConfigurator(activity, intent)
    }


    override fun onCreate(savedInstanceState: Bundle,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
    }

    override fun renderInternal(screenModel: MainScreenModel) {}
}
