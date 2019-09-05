package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex.bottom

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.sample.domain.SampleData
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultFragmentScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class ComplexBottomSheetDialogScreenConfigurator(args: Bundle) : DefaultFragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, ComplexBottomSheetDialogScreenModule::class])
    internal interface ComplexBottomSheetDialogScreenComponent
        : ScreenComponent<ComplexBottomSheetDialogFragment>

    @Module
    internal class ComplexBottomSheetDialogScreenModule(route: ComplexBottomSheetDialogRoute)
        : DefaultCustomScreenModule<ComplexBottomSheetDialogRoute>(route)

    override fun createScreenComponent(parentComponentDefault: DefaultActivityComponent,
                                       defaultFragmentScreenModule: DefaultFragmentScreenModule,
                                       args: Bundle): ScreenComponent<*> {
        return DaggerComplexBottomSheetDialogScreenConfigurator_ComplexBottomSheetDialogScreenComponent.builder()
                .defaultActivityComponent(parentComponentDefault)
                .defaultFragmentScreenModule(defaultFragmentScreenModule)
                .complexBottomSheetDialogScreenModule(
                        ComplexBottomSheetDialogScreenModule(
                                ComplexBottomSheetDialogRoute(
                                        args.getSerializable(Route.EXTRA_FIRST) as SampleData)))
                .build()
    }
}