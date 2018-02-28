package ru.surfstudio.standard.ui.screen.tabs.fragments.tab1

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.standard.ui.base.dagger.screen.FragmentScreenModule


class Tab1FragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?,
                                       args: Bundle?): ScreenComponent<*>? =
            DaggerTab1FragmentConfigurator_Tab1ScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .tab1FragmentModule(Tab1FragmentModule(Tab1FragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, Tab1FragmentModule::class])
    interface Tab1ScreenComponent : ScreenComponent<Tab1FragmentView> {}

    @Module
    class Tab1FragmentModule(route: Tab1FragmentRoute) : CustomScreenModule<Tab1FragmentRoute>(route)
}