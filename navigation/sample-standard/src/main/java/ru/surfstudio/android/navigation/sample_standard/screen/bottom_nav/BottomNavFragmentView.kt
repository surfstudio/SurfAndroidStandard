package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.fragment_bottom_nav.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.provider.container.TabFragmentNavigationContainer
import ru.surfstudio.android.navigation.sample_standard.R
import ru.surfstudio.android.navigation.sample_standard.utils.addOnBackPressedListener
import javax.inject.Inject

class BottomNavFragmentView : BaseRxFragmentView(), TabFragmentNavigationContainer {

    override val containerId: Int = R.id.bottom_nav_fragment_container

    @Inject
    lateinit var bm: BottomNavBindModel

    override fun createConfigurator() = BottomNavScreenConfigurator(arguments)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, viewRecreated: Boolean) {
        addOnBackPressedListener { bm.backPressed.accept() }
        home_tab_btn.clicks().bindTo { bm.bottomNavClicked.accept(BottomNavTabType.HOME) }
        profile_tab_btn.clicks().bindTo { bm.bottomNavClicked.accept(BottomNavTabType.PROFILE) }
    }
}