package ru.surfstudio.standard.f_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxFragmentView
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_profile.di.ProfileScreenConfigurator
import javax.inject.Inject

class ProfileFragmentView: BaseRxFragmentView(), CrossFeatureFragment {

    @Inject
    lateinit var bm: ProfileBindModel

    override fun createConfigurator() = ProfileScreenConfigurator()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

}