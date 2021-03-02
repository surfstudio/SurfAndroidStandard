package ru.surfstudio.android.navigation.sample_standard.screen.bottom_nav.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.navigation.sample_standard.R
import javax.inject.Inject

class ProfileFragmentView : BaseRxFragmentView() {

    @Inject
    lateinit var bm: ProfileBindModel

    override fun createConfigurator() = ProfileScreenConfigurator(arguments)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        profile_settings_btn.setOnClickListener { bm.openSettings.accept() }
        profile_logout_btn.setOnClickListener { bm.openConfirmLogoutScreen.accept() }
        profile_search_btn.setOnClickListener { bm.openSearch.accept() }
    }
}