package ru.surfstudio.android.navigation.sample_standard.screen.splash

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.navigation.sample_standard.R

class SplashActivityView : BaseRxActivityView() {

    override fun createConfigurator() = SplashScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_main

    override fun getScreenName(): String = "Splash"
}