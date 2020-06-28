package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home.nested

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home_nested.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.sample_standard.R
import javax.inject.Inject

class HomeNestedFragmentView : BaseRxFragmentView() {

    @Inject
    lateinit var bm: HomeNestedBindModel

    override fun createConfigurator() = HomeNestedScreenConfigurator(arguments)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_nested, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onActivityCreated(savedInstanceState, viewRecreated)
        bm.currentOrder bindTo ::setOrderText
        home_nested_add_btn.setOnClickListener { bm.openNextScreen.accept() }
    }

    private fun setOrderText(currentOrder: Int) {
        home_nested_tv.text = "Home\nNested\nFragment\n#$currentOrder"
    }

}