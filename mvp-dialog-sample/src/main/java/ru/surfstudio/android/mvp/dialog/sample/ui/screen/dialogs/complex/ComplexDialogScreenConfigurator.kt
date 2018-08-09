package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.navigation.Route.EXTRA_FIRST
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.sample.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.android.mvp.dialog.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.mvp.dialog.sample.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.mvp.dialog.sample.ui.base.dagger.screen.FragmentScreenModule
import ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.data.SampleData

class ComplexDialogScreenConfigurator(args: Bundle) : FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class], modules = [FragmentScreenModule::class, ComplexDialogScreenModule::class])
    internal interface ComplexDialogScreenComponent
        : ScreenComponent<ComplexDialogFragment>

    @Module
    internal class ComplexDialogScreenModule(route: ComplexDialogRoute)
        : CustomScreenModule<ComplexDialogRoute>(route)

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       fragmentScreenModule: FragmentScreenModule,
                                       args: Bundle): ScreenComponent<*> {
        return DaggerComplexDialogScreenConfigurator_ComplexDialogScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .complexDialogScreenModule(
                        ComplexDialogScreenModule(
                                ComplexDialogRoute(
                                        args.getSerializable(EXTRA_FIRST) as SampleData)))
                .build()
    }
}