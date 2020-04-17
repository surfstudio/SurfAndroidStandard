package ru.surfstudio.android.navigation.sample_standard.screen.splash

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.navigation.provider.id.IdentifiableScreen
import ru.surfstudio.android.navigation.sample_standard.R
import ru.surfstudio.android.navigation.sample_standard.screen.base.BaseSampleActivityView
import ru.surfstudio.android.navigation.sample_standard.screen.main.MainConfigurator

class SplashActivityView : BaseSampleActivityView() {

    override fun createConfigurator() = SplashConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_main
}