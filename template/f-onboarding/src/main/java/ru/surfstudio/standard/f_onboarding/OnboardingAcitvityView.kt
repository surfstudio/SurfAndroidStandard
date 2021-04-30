package ru.surfstudio.standard.f_onboarding

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvi.event.hub.owner.SingleHubOwner
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.standard.f_onboarding.databinding.ActivityOnboardingBinding
import ru.surfstudio.standard.f_onboarding.di.OnboardingScreenConfigurator
import ru.surfstudio.standard.ui.mvi.view.BaseMviActivityView
import javax.inject.Inject

internal class OnboardingAcitvityView: BaseMviActivityView<OnboardingStateHolder, OnboardingEvent>(),
        SingleHubOwner<OnboardingEvent> {

    @Inject
    override lateinit var hub: ScreenEventHub<OnboardingEvent>

    @Inject
    override lateinit var sh: State<OnboardingStateHolder>

    private val binding by viewBinding(ActivityOnboardingBinding::bind) { rootView }

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_onboarding

    override fun createConfigurator() = OnboardingScreenConfigurator()

    override fun getScreenName(): String = "OnBoardingActivityView"

    override fun render(state: OnboardingStateHolder) {
        //TODO расширить реализацию при создании приложения
    }

    override fun initViews() {
        //TODO расширить реализацию при создании приложения
    }


}
