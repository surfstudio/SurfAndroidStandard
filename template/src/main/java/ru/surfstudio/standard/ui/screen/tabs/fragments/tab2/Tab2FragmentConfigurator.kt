package ru.surfstudio.standard.ui.screen.tabs.fragments.tab2

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.standard.ui.base.dagger.screen.FragmentScreenModule


class Tab2FragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?,
                                       args: Bundle?): ScreenComponent<*>? =
            DaggerTab2FragmentConfigurator_Tab2ScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .tab2FragmentModule(Tab2FragmentModule(Tab2FragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, Tab2FragmentModule::class])
    interface Tab2ScreenComponent : ScreenComponent<Tab2FragmentView> {}

    @Module
    class Tab2FragmentModule(route: Tab2FragmentRoute) : CustomScreenModule<Tab2FragmentRoute>(route)
}