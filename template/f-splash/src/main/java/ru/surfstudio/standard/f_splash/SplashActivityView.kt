package ru.surfstudio.standard.f_splash

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.android.synthetic.main.activity_splash.view.*
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.android.template.f_splash.R
import ru.surfstudio.android.template.f_splash.databinding.ActivitySplashBinding
import ru.surfstudio.standard.f_splash.di.SplashScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import ru.surfstudio.standard.ui.util.view_binding.viewBinding
import javax.inject.Inject

internal class SplashActivityView : BaseMviActivityView<SplashState, SplashEvent>(), PushHandlingActivity {

    @Inject
    override lateinit var hub: ScreenEventHub<SplashEvent>

    @Inject
    override lateinit var sh: SplashScreenStateHolder

    private val binding: ActivitySplashBinding by viewBinding(ActivitySplashBinding::bind) { rootView }

    override fun getScreenName(): String = "SplashActivityView"

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_splash

    override fun render(state: SplashState) {

    }

    override fun initViews() {
    }
}