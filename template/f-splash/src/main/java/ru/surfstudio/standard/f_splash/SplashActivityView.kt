package ru.surfstudio.standard.f_splash

import `is`.mdk.app.f_splash.R
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.notification.ui.notification.PushHandlingActivity
import ru.surfstudio.standard.base_ui.provider.component.ComponentProvider
import javax.inject.Inject

class SplashActivityView : CoreActivityView(), PushHandlingActivity {

    @Inject
    internal lateinit var presenter: SplashPresenter

    @LayoutRes
    override fun getContentView(): Int {
        return R.layout.activity_splash
    }

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    override fun getScreenName(): String {
        return "splash"
    }

}