package ru.surfstudio.standard.f_search.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_search.SearchFragmentView
import ru.surfstudio.standard.f_search.SearchPresenter
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.navigation.SearchFragmentRoute
import ru.surfstudio.standard.ui.screen.CustomScreenModule
import ru.surfstudio.standard.ui.screen.FragmentScreenModule

class SearchScreenConfigurator : FragmentScreenConfigurator(Bundle.EMPTY) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, SearchScreenModule::class])
    interface SearchScreenComponent : BindableScreenComponent<SearchFragmentView>

    @Module
    class SearchScreenModule(route: SearchFragmentRoute) :
            CustomScreenModule<SearchFragmentRoute>(route) {

        @Provides
        @PerScreen
        fun provideSearchPresenter(presenter: SearchPresenter) = Any()
    }

    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: FragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerSearchScreenConfigurator_SearchScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .searchScreenModule(SearchScreenModule(SearchFragmentRoute()))
                .build()
    }
}