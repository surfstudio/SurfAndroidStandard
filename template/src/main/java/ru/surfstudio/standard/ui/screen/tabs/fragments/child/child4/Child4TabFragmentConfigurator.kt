package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child4

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


class Child4TabFragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?, args: Bundle?): ScreenComponent<*>? =
            DaggerChild4TabFragmentConfigurator_Child4TabScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .child4TabFragmentModule(Child4TabFragmentModule(Child4TabFragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, Child4TabFragmentModule::class])
    interface Child4TabScreenComponent : ScreenComponent<Child4TabFragmentView>{}

    @Module
    class Child4TabFragmentModule(route: Child4TabFragmentRoute) : CustomScreenModule<Child4TabFragmentRoute>(route) {
        init {
            Logger.d("2222 id from module ${route.id}")
        }
    }
}