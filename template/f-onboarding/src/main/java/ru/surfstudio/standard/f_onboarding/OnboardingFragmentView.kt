package ru.surfstudio.standard.f_onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.jakewharton.rxbinding2.view.clicks
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.standard.f_onboarding.databinding.FragmentOnboardingBinding
import ru.surfstudio.standard.f_onboarding.di.OnboardingScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviFragmentView
import javax.inject.Inject

/**
 * Вью экрана онбординга
 */
internal class OnboardingFragmentView : BaseMviFragmentView<OnboardingState, OnboardingEvent>(), CrossFeatureFragment {

    @Inject
    override lateinit var hub: ScreenEventHub<OnboardingEvent>

    @Inject
    override lateinit var sh: OnboardingStateHolder

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_onboarding, container, false)

    override fun createConfigurator() = OnboardingScreenConfigurator(bundleOf())

    override fun getScreenName(): String = "OnboardingFragmentView"

    override fun initViews() {
        //TODO расширить реализацию при создании приложения
        binding.onboardingRemindLaterBtn.clicks().emit(OnboardingEvent.Input.RemindLaterBtnClicked)
        binding.onboardingSkipBtn.clicks().emit(OnboardingEvent.Input.SkipBtnClicked)
    }

    override fun render(state: OnboardingState) {
        //TODO расширить реализацию при создании приложения
    }
}
