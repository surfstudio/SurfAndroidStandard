package ru.surfstudio.standard.f_profile.di

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
import ru.surfstudio.standard.f_profile.*
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.navigation.routes.ProfileFragmentRoute
import ru.surfstudio.standard.ui.screen_modules.CustomScreenModule
import ru.surfstudio.standard.ui.screen_modules.FragmentScreenModule

/**
 * Конфигуратор таба профиль [ProfileFragmentView]
 */
internal class ProfileScreenConfigurator(arguments: Bundle?) : FragmentScreenConfigurator(arguments) {

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, ProfileScreenModule::class]
    )
    internal interface ProfileScreenComponent : BindableScreenComponent<ProfileFragmentView>

    @Module
    internal class ProfileScreenModule(route: ProfileFragmentRoute) : CustomScreenModule<ProfileFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
                screenEventHubDependency: ScreenEventHubDependency
        ) = ScreenEventHub<ProfileEvent>(screenEventHubDependency, ProfileEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
                screenBinderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<ProfileEvent>,
                mw: ProfileMiddleware,
                sh: ProfileScreenStateHolder,
                reducer: ProfileReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, mw, sh, reducer)
        }
    }

    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: FragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerProfileScreenConfigurator_ProfileScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .profileScreenModule(ProfileScreenModule(ProfileFragmentRoute()))
                .build()
    }
}
