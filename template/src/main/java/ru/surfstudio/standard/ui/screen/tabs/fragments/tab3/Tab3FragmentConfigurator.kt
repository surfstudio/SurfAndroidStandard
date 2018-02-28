package ru.surfstudio.standard.ui.screen.tabs.fragments.tab3

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.standard.ui.base.dagger.screen.FragmentScreenModule


class Tab3FragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?,
                                       args: Bundle?): ScreenComponent<*>? =
            DaggerTab3FragmentConfigurator_Tab3ScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .tab3FragmentModule(Tab3FragmentModule(Tab3FragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, Tab3FragmentModule::class])
    interface Tab3ScreenComponent : ScreenComponent<Tab3FragmentView> {}

    @Module
    class Tab3FragmentModule(route: Tab3FragmentRoute) : CustomScreenModule<Tab3FragmentRoute>(route)
}