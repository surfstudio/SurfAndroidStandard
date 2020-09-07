package ru.surfstudio.standard.f_feed.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_feed.FeedFragmentView
import ru.surfstudio.standard.f_feed.FeedPresenter
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.navigation.FeedFragmentRoute
import ru.surfstudio.standard.ui.screen.CustomScreenModule
import ru.surfstudio.standard.ui.screen.FragmentScreenModule

class FeedScreenConfigurator : FragmentScreenConfigurator(Bundle.EMPTY) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, FeedScreenModule::class])
    interface FeedScreenComponent : BindableScreenComponent<FeedFragmentView>

    @Module
    class FeedScreenModule(route: FeedFragmentRoute) : CustomScreenModule<FeedFragmentRoute>(route) {

        @PerScreen
        @Provides
        fun provideFeedPresenter(presenter: FeedPresenter) = Any()
    }

    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: FragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerFeedScreenConfigurator_FeedScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .feedScreenModule(FeedScreenModule(FeedFragmentRoute()))
                .build()
    }
}