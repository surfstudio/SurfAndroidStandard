package ru.surfstudio.standard.f_onboarding.di

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_onboarding.*
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.navigation.routes.OnboardingActivityRoute
import ru.surfstudio.standard.ui.screen_modules.ActivityScreenModule
import ru.surfstudio.standard.ui.screen_modules.CustomScreenModule

/**
 * Конфигуратор [OnboardingActivityView].
 */
internal class OnboardingScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, OnboardingScreenModule::class]
    )
    internal interface OnboardingScreenComponent : BindableScreenComponent<OnboardingActivityView>

    @Module
    internal class OnboardingScreenModule(route: OnboardingActivityRoute) :
            CustomScreenModule<OnboardingActivityRoute>(route) {

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

    override fun createScreenComponent(parentActivityComponent: ActivityComponent?,
                                       activityScreenModule: ActivityScreenModule?,
                                       intent: Intent?): OnboardingScreenComponent {
        return DaggerOnboardingScreenConfigurator_OnboardingScreenComponent.builder()
                .activityComponent(parentActivityComponent)
                .activityScreenModule(activityScreenModule)
                .onboardingScreenModule(OnboardingScreenModule(OnboardingActivityRoute()))
                .build()
    }
}
