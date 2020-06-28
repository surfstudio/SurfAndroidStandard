package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.profile

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.configurator.FragmentScreenConfigurator
import ru.surfstudio.android.navigation.sample_standard.di.ui.screen.ScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class ProfileScreenConfigurator(args: Bundle?): FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, ProfileScreenModule::class])
    internal interface ProfileFragmentScreenComponent
        : BindableScreenComponent<ProfileFragmentView>

    @Module
    internal class ProfileScreenModule : ScreenModule() {

        @Provides
        @PerScreen
        fun providePresenters(presenter: ProfilePresenter): Any = presenter
    }


    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerProfileScreenConfigurator_ProfileFragmentScreenComponent.builder()
                .activityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .profileScreenModule(ProfileScreenModule())
                .build()
    }
}