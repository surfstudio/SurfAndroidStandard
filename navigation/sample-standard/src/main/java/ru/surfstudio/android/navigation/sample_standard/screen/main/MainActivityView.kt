package ru.surfstudio.android.navigation.sample_standard.screen.main

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.sample_standard.R

class MainActivityView : BaseRxActivityView(), FragmentNavigationContainer {

    override val containerId: Int = R.id.fragment_container

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_main

    override fun getScreenName(): String = "Main"
}