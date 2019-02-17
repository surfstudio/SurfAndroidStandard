package ru.surfstudio.android.app.migration.sample.ui.screen.splash

import androidx.annotation.LayoutRes
import ru.surfstudio.android.app.migration.sample.R
import ru.surfstudio.android.app.migration.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import javax.inject.Inject

/**
 * Вью загрузочного экрана
 */
class SplashActivityView : BaseRenderableActivityView<SplashScreenModel>() {

    @Inject internal lateinit var presenter: SplashPresenter

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_splash

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = SplashScreenConfigurator(intent)

    override fun getScreenName() = "splash"

    override fun renderInternal(sm: SplashScreenModel) {
        // Render splash screen
    }
}
