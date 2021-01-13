package ru.surfstudio.standard.f_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.standard.f_profile.databinding.FragmentProfileBinding
import ru.surfstudio.standard.f_profile.di.ProfileScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviFragmentView
import javax.inject.Inject

/**
 * Вью таба профиль
 */
internal class ProfileFragmentView : BaseMviFragmentView<ProfileState, ProfileEvent>(), CrossFeatureFragment {

    @Inject
    override lateinit var hub: ScreenEventHub<ProfileEvent>

    @Inject
    override lateinit var sh: ProfileScreenStateHolder

    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun createConfigurator() = ProfileScreenConfigurator(arguments)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun initViews() {
    }

    override fun render(state: ProfileState) {

    }
}