package ru.surfstudio.standard.f_main.fragment2

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.FragmentScreenConfigurator
import ru.surfstudio.standard.ui.screen.CustomScreenModule
import ru.surfstudio.standard.ui.screen.FragmentScreenModule

class Fragment2Configurator(bundle: Bundle) : FragmentScreenConfigurator(bundle) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
        modules = [FragmentScreenModule::class, Fragment2Module::class])
    internal interface Fragment2Component : ScreenComponent<Fragment2View>

    @Module
    internal class Fragment2Module(route: Fragment2Route) : CustomScreenModule<Fragment2Route>(route)

    override fun createScreenComponent(
        parentComponent: ActivityComponent?,
        fragmentScreenModule: FragmentScreenModule?,
        args: Bundle?
    ): ScreenComponent<*> {
        return DaggerFragment2Configurator_Fragment2Component.builder()
            .activityComponent(parentComponent)
            .fragmentScreenModule(fragmentScreenModule)
            .fragment2Module(Fragment2Module(Fragment2Route()))
            .build()
    }
}