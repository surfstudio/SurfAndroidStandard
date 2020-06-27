package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.sample_standard.R
import javax.inject.Inject

class BottomNavigationFragmentView : BaseRxFragmentView(), FragmentNavigationContainer {

    override val containerId: Int = R.id.bottom_nav_fragment_container

    @Inject
    lateinit var bm: BottomNavigationBindModel

    override fun createConfigurator() = BottomNavScreenConfigurator(arguments)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {

    }

}