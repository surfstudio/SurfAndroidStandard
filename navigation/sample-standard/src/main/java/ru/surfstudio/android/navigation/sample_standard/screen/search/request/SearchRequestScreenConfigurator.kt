package ru.surfstudio.android.navigation.sample_standard.screen.search.request

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

class SearchRequestScreenConfigurator(args: Bundle): FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, SearchRequestScreenModule::class])
    internal interface SearchRequestFragmentScreenComponent
        : BindableScreenComponent<SearchRequestFragmentView>

    @Module
    internal class SearchRequestScreenModule(route: SearchRequestRoute) : RouteScreenModule<SearchRequestRoute>(route) {

        @Provides
        @PerScreen
        fun providePresenters(presenter: SearchRequestPresenter): Any = presenter
    }


    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle
    ): ScreenComponent<*> {
        return DaggerSearchRequestScreenConfigurator_SearchRequestFragmentScreenComponent.builder()
                .activityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .searchRequestScreenModule(SearchRequestScreenModule(SearchRequestRoute(args)))
                .build()
    }
}