package ru.surfstudio.standard.f_splash

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.android.template.f_splash.databinding.ActivitySplashBinding
import ru.surfstudio.standard.f_splash.di.SplashScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import javax.inject.Inject

/**
 * todo Класс публичный для работы инструментальных тестов
 */
class SplashActivityView : BaseMviActivityView<SplashState, SplashEvent>(), PushHandlingActivity {

   @Inject
   override lateinit var hub: ScreenEventHub<SplashEvent>

   @Inject
   override lateinit var sh: SplashScreenStateHolder

   private lateinit var binding: ActivitySplashBinding

    override fun getScreenName(): String = "SplashActivityView"

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding {
        binding = ActivitySplashBinding.inflate(inflater)
        return binding
    }

    override fun render(state: SplashState) {
    }

    override fun initViews() {
    }
}