package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.configurator.FragmentScreenConfigurator
import ru.surfstudio.android.navigation.sample_standard.dagger.ui.screen.ScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class BottomNavScreenConfigurator(args: Bundle?) : FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, BottomNavigationScreenModule::class])
    internal interface BottomNavFragmentScreenComponent
        : BindableScreenComponent<BottomNavigationFragmentView>

    @Module
    internal class BottomNavigationScreenModule : ScreenModule() {

        @Provides
        @PerScreen
        fun providePresenters(presenter: BottomNavigationPresenter): Any = presenter
    }

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerBottomNavScreenConfigurator_BottomNavFragmentScreenComponent.builder()
                .defaultActivityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .bottomNavigationScreenModule(BottomNavigationScreenModule())
                .build()
    }
}