package ru.surfstudio.android.navigation.sample_standard.screen.search.results

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

class SearchResultScreenConfigurator(args: Bundle): FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, SearchResultScreenModule::class])
    internal interface SearchResultFragmentScreenComponent
        : BindableScreenComponent<SearchResultFragmentView>

    @Module
    internal class SearchResultScreenModule(route: SearchResultRoute) : RouteScreenModule<SearchResultRoute>(route) {

        @Provides
        @PerScreen
        fun providePresenters(presenter: SearchResultPresenter): Any = presenter
    }


    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: DefaultFragmentScreenModule?,
            args: Bundle
    ): ScreenComponent<*> {
        return DaggerSearchResultScreenConfigurator_SearchResultFragmentScreenComponent.builder()
                .activityComponent(parentComponent)
                .defaultFragmentScreenModule(fragmentScreenModule)
                .searchResultScreenModule(SearchResultScreenModule(SearchResultRoute(args)))
                .build()
    }
}
