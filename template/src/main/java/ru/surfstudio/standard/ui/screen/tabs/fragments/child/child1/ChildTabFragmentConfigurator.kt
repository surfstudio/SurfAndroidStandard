package ru.surfstudio.standard.ui.screen.tabs.fragments.child.child1

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


class ChildTabFragmentConfigurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    override fun createScreenComponent(parentComponent: ActivityComponent?,
                                       fragmentScreenModule: FragmentScreenModule?, args: Bundle?): ScreenComponent<*>? =
            DaggerChildTabFragmentConfigurator_ChildTabScreenComponent.builder()
                    .activityComponent(parentComponent)
                    .fragmentScreenModule(fragmentScreenModule)
                    .childTabFragmentModule(ChildTabFragmentModule(ChildTabFragmentRoute(args!!)))
                    .build()

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, ChildTabFragmentModule::class])
    interface ChildTabScreenComponent : ScreenComponent<ChildTabFragmentView> {}

    @Module
    class ChildTabFragmentModule(route: ChildTabFragmentRoute) : CustomScreenModule<ChildTabFragmentRoute>(route) {
        init {
            Logger.d("2222 id from module ${route.id}")
        }
    }
}