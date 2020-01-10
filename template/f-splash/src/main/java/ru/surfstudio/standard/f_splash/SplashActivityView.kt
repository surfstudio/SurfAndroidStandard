package ru.surfstudio.standard.f_splash

import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.android.template.f_splash.R
import ru.surfstudio.standard.f_splash.di.SplashScreenConfigurator
import javax.inject.Inject

class SplashActivityView : CoreActivityView(), PushHandlingActivity {

    @Inject
    internal lateinit var presenter: SplashPresenter

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_splash

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun getScreenName(): String = "splash"
}