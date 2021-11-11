package ru.surfstudio.standard.f_onboarding.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_onboarding.*
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.navigation.routes.OnboardingFragmentRoute
import ru.surfstudio.standard.ui.screen_modules.ActivityScreenModule
import ru.surfstudio.standard.ui.screen_modules.CustomScreenModule
import ru.surfstudio.standard.ui.screen_modules.FragmentScreenModule

/**
 * Конфигуратор [OnboardingFragmentView].
 */
internal class OnboardingScreenConfigurator(bundle: Bundle?) : FragmentScreenConfigurator(bundle) {

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, OnboardingScreenModule::class]
    )
    internal interface OnboardingScreenComponent : BindableScreenComponent<OnboardingFragmentView>

    @Module
    internal class OnboardingScreenModule(route: OnboardingFragmentRoute) :
            CustomScreenModule<OnboardingFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(screenEventHubDependency: ScreenEventHubDependency) =
                ScreenEventHub<OnboardingEvent>(screenEventHubDependency, OnboardingEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
                screenBinderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<OnboardingEvent>,
                middleware: OnboardingMiddleware,
                reducer: OnboardingReducer,
                stateHolder: OnboardingStateHolder
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, middleware, stateHolder, reducer)
        }
    }

    override fun createScreenComponent(
            activityComponent: ActivityComponent?,
            fragmentScreenModule: FragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerOnboardingScreenConfigurator_OnboardingScreenComponent.builder()
                .activityComponent(activityComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .onboardingScreenModule(OnboardingScreenModule(OnboardingFragmentRoute()))
                .build()
    }
}
