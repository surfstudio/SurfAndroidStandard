package ru.surfstudio.standard.f_splash

import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.android.template.f_splash.R
import ru.surfstudio.standard.f_splash.di.SplashScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import javax.inject.Inject

internal class SplashActivityView : BaseMviActivityView<SplashState, SplashEvent>(), PushHandlingActivity {

    @Inject
    override lateinit var hub: ScreenEventHub<SplashEvent>

    @Inject
    override lateinit var sh: SplashScreenStateHolder

    override fun getScreenName(): String = "SplashActivityView"

    override fun getContentView(): Int = R.layout.activity_splash

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun render(state: SplashState) {
    }

    override fun initViews() {
    }
}