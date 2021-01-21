package ru.surfstudio.standard.f_main.bar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.rivegauche.app.f_main.bar.MainBarState
import ru.rivegauche.app.f_main.bar.MainBarStateHolder
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.navigation.provider.container.TabFragmentNavigationContainer
import ru.surfstudio.android.template.f_main.R
import ru.surfstudio.android.template.f_main.databinding.FragmentMainBinding
import ru.surfstudio.standard.f_main.bar.MainBarEvent.TabSelected
import ru.surfstudio.standard.f_main.bar.di.MainBarScreenConfigurator
import ru.surfstudio.standard.f_main.view.BottomBarView
import ru.surfstudio.standard.ui.mvi.view.BaseMviFragmentView
import ru.surfstudio.standard.ui.util.performIfChanged
import javax.inject.Inject

/**
 * Вью главного экрана
 */
internal class MainBarFragmentView : BaseMviFragmentView<MainBarState, MainBarEvent>(),
        TabFragmentNavigationContainer,
        CrossFeatureFragment {

    @Inject
    override lateinit var hub: ScreenEventHub<MainBarEvent>

    @Inject
    override lateinit var sh: MainBarStateHolder

    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun createConfigurator() = MainBarScreenConfigurator(arguments)

    override val containerId: Int = R.id.fragment_container

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun getScreenName(): String = "MainBarFragmentView"

    override fun initViews() {
        initListeners()
    }

    override fun render(state: MainBarState) {
        binding.mainBottomBar.performIfChanged(state.selectedTab, BottomBarView::updateSelection)
    }

    private fun initListeners() {
        binding.mainBottomBar.tabSelectedAction = { TabSelected(it).emit() }
    }
}
