package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child2

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


class Child2TabFragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?, args: Bundle?): ScreenComponent<*>? =
            DaggerChild2TabFragmentConfigurator_Child2TabScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .child2TabFragmentModule(Child2TabFragmentModule(Child2TabFragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, Child2TabFragmentModule::class])
    interface Child2TabScreenComponent : ScreenComponent<Child2TabFragmentView> {}

    @Module
    class Child2TabFragmentModule(route: Child2TabFragmentRoute) : CustomScreenModule<Child2TabFragmentRoute>(route) {
        init {
            Logger.d("2222 id from module ${route.id}")
        }
    }
}