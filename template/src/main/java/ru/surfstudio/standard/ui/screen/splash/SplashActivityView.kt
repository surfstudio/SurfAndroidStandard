package ru.surfstudio.standard.ui.screen.splash

import android.support.annotation.LayoutRes
import ru.surfstudio.android.core.ui.base.screen.activity.BaseHandleableErrorActivityView
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

/**
 * Сплэш активити.
 */
class SplashActivityView : BaseHandleableErrorActivityView() {
    @Inject internal lateinit var presenter: SplashPresenter

    @LayoutRes
    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return SplashScreenConfigurator(intent)
    }

    override fun getName(): String {
        return "splash"
    }

}
