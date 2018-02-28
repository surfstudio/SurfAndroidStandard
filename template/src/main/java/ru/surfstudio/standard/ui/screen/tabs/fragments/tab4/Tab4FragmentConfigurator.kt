package ru.surfstudio.standard.ui.screen.tabs.fragments.tab4

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.standard.ui.base.dagger.screen.FragmentScreenModule


class Tab4FragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?,
                                       args: Bundle?): ScreenComponent<*>? =
            DaggerTab4FragmentConfigurator_Tab4ScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .tab4FragmentModule(Tab4FragmentModule(Tab4FragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, Tab4FragmentModule::class])
    interface Tab4ScreenComponent : ScreenComponent<Tab4FragmentView> {}

    @Module
    class Tab4FragmentModule(route: Tab4FragmentRoute) : CustomScreenModule<Tab4FragmentRoute>(route)
}