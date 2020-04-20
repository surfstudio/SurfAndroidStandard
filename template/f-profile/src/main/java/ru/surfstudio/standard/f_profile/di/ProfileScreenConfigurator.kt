package ru.surfstudio.standard.f_profile.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_profile.ProfileFragmentView
import ru.surfstudio.standard.f_profile.ProfilePresenter
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.navigation.ProfileFragmentRoute
import ru.surfstudio.standard.ui.screen.CustomScreenModule
import ru.surfstudio.standard.ui.screen.FragmentScreenModule

class ProfileScreenConfigurator : FragmentScreenConfigurator(Bundle.EMPTY) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, ProfileScreenModule::class])
    interface ProfileScreenComponent : BindableScreenComponent<ProfileFragmentView>

    @Module
    class ProfileScreenModule(route: ProfileFragmentRoute) :
            CustomScreenModule<ProfileFragmentRoute>(route) {

        @PerScreen
        @Provides
        fun provideProfilePresenter(presenter: ProfilePresenter) = Any()
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