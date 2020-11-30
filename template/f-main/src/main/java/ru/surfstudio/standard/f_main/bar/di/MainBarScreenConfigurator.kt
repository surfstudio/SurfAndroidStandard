package ru.surfstudio.standard.f_main.bar.di

import android.os.Bundle
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.rivegauche.app.f_main.bar.MainBarMiddleware
import ru.rivegauche.app.f_main.bar.MainBarReducer
import ru.rivegauche.app.f_main.bar.MainBarStateHolder
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_main.bar.MainBarEvent
import ru.surfstudio.standard.f_main.bar.MainBarFragmentView
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.navigation.routes.MainBarRoute
import ru.surfstudio.standard.ui.screen_modules.CustomScreenModule
import ru.surfstudio.standard.ui.screen_modules.FragmentScreenModule

/**
 * Конфигуратор экрана логина [MainBarFragmentView]
 */
internal class MainBarScreenConfigurator(arguments: Bundle?) : FragmentScreenConfigurator(arguments) {

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, MainBarScreenModule::class]
    )
    internal interface MainBarScreenComponent : BindableScreenComponent<MainBarFragmentView>

    @Module
    internal class MainBarScreenModule(route: MainBarRoute) : CustomScreenModule<MainBarRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
                screenEventHubDependency: ScreenEventHubDependency
        ) = ScreenEventHub<MainBarEvent>(screenEventHubDependency, MainBarEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
                screenBinderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<MainBarEvent>,
                mw: MainBarMiddleware,
                sh: MainBarStateHolder,
                reducer: MainBarReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, mw, sh, reducer)
        }
    }

    override fun createScreenComponent(
            parentComponent: ActivityComponent?,
            fragmentScreenModule: FragmentScreenModule?,
            args: Bundle?
    ): ScreenComponent<*> {
        return DaggerMainBarScreenConfigurator_MainBarScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .mainBarScreenModule(MainBarScreenModule(MainBarRoute()))
                .build()
    }
}
