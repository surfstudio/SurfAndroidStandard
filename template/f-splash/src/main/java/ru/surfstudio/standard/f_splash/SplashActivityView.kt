package ru.surfstudio.standard.f_splash

import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.android.template.f_splash.R
import ru.surfstudio.android.template.f_splash.databinding.ActivitySplashBinding
import ru.surfstudio.standard.f_splash.di.SplashScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import javax.inject.Inject

internal class SplashActivityView : BaseMviActivityView<SplashState, SplashEvent>(),
        PushHandlingActivity, FragmentNavigationContainer {

    @Inject
    override lateinit var hub: ScreenEventHub<SplashEvent>

    @Inject
    override lateinit var sh: SplashScreenStateHolder

    private val binding: ActivitySplashBinding by viewBinding(ActivitySplashBinding::bind) { rootView }

    override fun getScreenName(): String = "SplashActivityView"

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_splash

    override val containerId: Int = R.id.splash_fragment_container

    override fun render(state: SplashState) {

    }

    override fun initViews() {
    }
}