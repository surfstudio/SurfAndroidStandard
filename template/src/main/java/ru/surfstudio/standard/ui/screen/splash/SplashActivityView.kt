package ru.surfstudio.standard.ui.screen.splash

import android.app.Activity
import android.content.Intent
import android.support.annotation.LayoutRes
import ru.surfstudio.android.core.ui.base.screen.activity.BaseHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.standard.ui.base.configurator.ActivityConfigurator
import javax.inject.Inject

/**
 * Сплэш активити.
 */
class SplashActivityView : BaseHandleableErrorActivityView() {
    @Inject internal lateinit var presenter: SplashPresenter

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createScreenConfigurator(activity: Activity, intent: Intent): ScreenConfigurator<*> {
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
