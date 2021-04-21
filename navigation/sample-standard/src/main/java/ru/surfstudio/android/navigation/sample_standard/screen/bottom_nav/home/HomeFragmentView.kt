package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.sample_standard.R
import javax.inject.Inject

class HomeFragmentView : BaseRxFragmentView() {

    @Inject
    lateinit var bm: HomeBindModel

    override fun createConfigurator() = HomeScreenConfigurator(arguments)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, viewRecreated: Boolean) {
        home_add_btn.setOnClickListener { bm.openNestedScreenAction.accept() }
    }
}