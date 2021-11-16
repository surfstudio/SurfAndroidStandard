package ru.surfstudio.standard.f_onboarding

import com.jakewharton.rxbinding2.view.clicks
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.ui.navigation.feature.route.feature.CrossFeatureFragment
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.standard.f_onboarding.databinding.ActivityOnboardingBinding
import ru.surfstudio.standard.f_onboarding.di.OnboardingScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import javax.inject.Inject

/**
 * Вью экрана онбординга
 */
internal class OnboardingActivityView : BaseMviActivityView<OnboardingState, OnboardingEvent>(), CrossFeatureFragment {

    @Inject
    override lateinit var hub: ScreenEventHub<OnboardingEvent>

    @Inject
    override lateinit var sh: OnboardingStateHolder

    private val binding by viewBinding(ActivityOnboardingBinding::bind) { rootView }

    override fun getContentView(): Int = R.layout.activity_onboarding

    override fun createConfigurator() = OnboardingScreenConfigurator(intent)

    override fun getScreenName(): String = "OnboardingActivityView"

    override fun initViews() {
        //TODO расширить реализацию при создании приложения
        binding.onboardingRemindLaterBtn.clicks().emit(OnboardingEvent.Input.RemindLaterBtnClicked)
        binding.onboardingSkipBtn.clicks().emit(OnboardingEvent.Input.SkipBtnClicked)
    }

    override fun render(state: OnboardingState) {
        //TODO расширить реализацию при создании приложения
    }
}
