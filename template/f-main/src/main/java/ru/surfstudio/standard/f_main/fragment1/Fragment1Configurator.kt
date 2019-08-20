package ru.surfstudio.standard.f_main.fragment1

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.screen.CustomScreenModule
import ru.surfstudio.standard.ui.screen.FragmentScreenModule

class Fragment1Configurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
        modules = [FragmentScreenModule::class, Fragment1Module::class])
    internal interface Fragment1Component : ScreenComponent<Fragment1View>

    @Module
    internal class Fragment1Module(route: Fragment1Route) : CustomScreenModule<Fragment1Route>(route)

    override fun createScreenComponent(
        parentComponent: ActivityComponent?,
        fragmentScreenModule: FragmentScreenModule?,
        args: Bundle?
    ): ScreenComponent<*> {
        return DaggerFragment1Configurator_Fragment1Component.builder()
            .activityComponent(parentComponent)
            .fragmentScreenModule(fragmentScreenModule)
            .fragment1Module(Fragment1Module(Fragment1Route()))
            .build()
    }
}