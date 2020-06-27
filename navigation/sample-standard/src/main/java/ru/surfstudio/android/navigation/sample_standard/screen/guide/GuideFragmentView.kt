package ru.surfstudio.android.navigation.sample_standard.screen.guide

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.fragment_guide.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.sample_standard.R
import javax.inject.Inject

class GuideFragmentView : BaseRxFragmentView() {

    @Inject
    lateinit var bm: GuideBindModel

    override fun createConfigurator() = GuideScreenConfigurator(arguments)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        guide_bottom_nav_btn.clicks() bindTo {
            Log.d("111111", "Bottom click")
            bm.bottomNavClicked.accept()
        }
        guide_shared_transition_btn.clicks() bindTo { bm.sharedElementClicked.accept() }
    }

}