package ru.surfstudio.android.navigation.sample_standard.screen.bottom_navigation.home.nested

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.sample_standard.di.ui.ActivityComponent
import ru.surfstudio.android.navigation.sample_standard.di.ui.configurator.FragmentScreenConfigurator
import ru.surfstudio.android.navigation.sample_standard.di.ui.screen.RouteScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class HomeNestedScreenConfigurator(args: Bundle?) : FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, HomeNestedScreenModule::class])
    internal interface HomeNestedFragmentScreenComponent
        : BindableScreenComponent<HomeNestedFragmentView>

    @Module
    internal class HomeNestedScreenModule(route: HomeNestedRoute) : RouteScreenModule<HomeNestedRoute>(route) {

        @Provides
        @PerScreen
        fun providePresenters(presenter: HomeNestedPresenter): Any = presenter
    }


    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerHomeNestedScreenConfigurator_HomeNestedFragmentScreenComponent.builder()
                .activityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .homeNestedScreenModule(HomeNestedScreenModule(HomeNestedRoute(args)))
                .build()
    }
}