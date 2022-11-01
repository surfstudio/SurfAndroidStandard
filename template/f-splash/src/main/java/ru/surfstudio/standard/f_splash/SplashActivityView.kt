package ru.surfstudio.standard.f_splash

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.android.template.f_splash.R
import ru.surfstudio.android.utilktx.util.SdkUtils
import ru.surfstudio.standard.f_splash.di.SplashScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
internal class SplashActivityView : BaseMviActivityView<SplashState, SplashEvent>(), PushHandlingActivity {

    @Inject
    override lateinit var hub: ScreenEventHub<SplashEvent>

    @Inject
    override lateinit var sh: SplashScreenStateHolder

    override fun getScreenName(): String = "SplashActivityView"

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_splash

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        if (SdkUtils.isAtLeastS()) {
            // требуется вызывать этот метод до вызова setContentView(..),
            // который вызывается в super.onCreate(..)
            installSplashScreen()
        }
        super.onCreate(savedInstanceState, persistentState, viewRecreated)

        if (SdkUtils.isAtLeastS()) {
            // не показываем этот экран, т.к. отображается системный сплеш
            val content: View = findViewById(android.R.id.content)
            content.viewTreeObserver.addOnPreDrawListener { false }
        }
    }

    override fun render(state: SplashState) {

    }

    override fun initViews() {
    }
}