package ru.surfstudio.android.navigation.sample_standard.screen.main

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.navigation.provider.id.IdentifiableScreen
import ru.surfstudio.android.navigation.sample_standard.R
import ru.surfstudio.android.navigation.sample_standard.screen.base.BaseSampleActivityView

class MainActivityView : BaseSampleActivityView() {

    override fun createConfigurator() = MainConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_main
}