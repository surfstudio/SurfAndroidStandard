package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.bottom

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.sample.domain.SampleData
import ru.surfstudio.android.sample.dagger.ui.base.configurator.FragmentScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.FragmentScreenModule

class ComplexBottomSheetDialogScreenConfigurator(args: Bundle) : FragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [FragmentScreenModule::class, ComplexBottomSheetDialogScreenModule::class])
    internal interface ComplexBottomSheetDialogScreenComponent
        : ScreenComponent<ComplexBottomSheetDialogFragment>

    @Module
    internal class ComplexBottomSheetDialogScreenModule(route: ComplexBottomSheetDialogRoute)
        : CustomScreenModule<ComplexBottomSheetDialogRoute>(route)

    override fun createScreenComponent(parentComponent: ActivityComponent,
                                       fragmentScreenModule: FragmentScreenModule,
                                       args: Bundle): ScreenComponent<*> {
        return DaggerComplexBottomSheetDialogScreenConfigurator_ComplexBottomSheetDialogScreenComponent.builder()
                .activityComponent(parentComponent)
                .fragmentScreenModule(fragmentScreenModule)
                .complexBottomSheetDialogScreenModule(
                        ComplexBottomSheetDialogScreenModule(
                                ComplexBottomSheetDialogRoute(
                                        args.getSerializable(Route.EXTRA_FIRST) as SampleData)))
                .build()
    }
}