package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child3

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.standard.ui.base.dagger.screen.FragmentScreenModule


class Child3TabFragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?, args: Bundle?): ScreenComponent<*>? =
            DaggerChild3TabFragmentConfigurator_Child3TabScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .child3TabFragmentModule(Child3TabFragmentModule(Child3TabFragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, Child3TabFragmentModule::class])
    interface Child3TabScreenComponent : ScreenComponent<Child3TabFragmentView> {}

    @Module
    class Child3TabFragmentModule(route: Child3TabFragmentRoute) : CustomScreenModule<Child3TabFragmentRoute>(route) {
        init {
            Logger.d("2222 id from module ${route.id}")
        }
    }
}