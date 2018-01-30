package ru.surfstudio.standard.ui.screen.splash

import android.app.Activity
import android.content.Intent
import android.support.annotation.LayoutRes
import ru.surfstudio.android.core.ui.base.screen.activity.BaseHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator
import ru.surfstudio.android.core.ui.base.screen.configurator.ViewConfigurator
import ru.surfstudio.standard.R
import javax.inject.Inject

/**
 * Сплэш активити.
 */
class SplashActivityView : BaseHandleableErrorActivityView() {
    @Inject internal lateinit var presenter: SplashPresenter

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createScreenConfigurator(activity: Activity, intent: Intent): ViewConfigurator<*> {
        return SplashScreenConfigurator(activity, intent)
    }

    @LayoutRes
    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    override fun createActivityConfigurator(): BaseActivityConfigurator<*, *> {
        return ActivityConfigurator(this)
    }
}
