package ru.surfstudio.standard.f_onboarding

import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.standard.f_onboarding.databinding.ActivityOnboardingBinding
import ru.surfstudio.standard.f_onboarding.di.OnboardingScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import javax.inject.Inject

internal class OnboardingActivityView : BaseMviActivityView<OnboardingState, OnboardingEvent>() {

    @Inject
    override lateinit var hub: ScreenEventHub<OnboardingEvent>

    @Inject
    override lateinit var sh: State<OnboardingState>

    private val binding by viewBinding(ActivityOnboardingBinding::bind) { rootView }

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_onboarding

    override fun createConfigurator() = OnboardingScreenConfigurator(intent)

    override fun getScreenName(): String = "OnboardingActivityView"

    override fun initViews() {
        //TODO расширить реализацию при создании приложения
    }

    override fun render(state: OnboardingState) {
        //TODO расширить реализацию при создании приложения
    }


}
