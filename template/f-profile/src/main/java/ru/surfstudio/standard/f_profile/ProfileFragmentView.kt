package ru.surfstudio.standard.f_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvp.fragment.BaseRenderableFragmentView
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.standard.f_profile.di.ProfileScreenConfigurator
import javax.inject.Inject

class ProfileFragmentView: BaseRenderableFragmentView<ProfileScreenModel>(), CrossFeatureFragment {

    @Inject
    lateinit var presenter: ProfilePresenter

    override fun createConfigurator() = ProfileScreenConfigurator()

    override fun getPresenters()= arrayOf(presenter)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun renderInternal(sm: ProfileScreenModel?) {
        // TODO
    }
}