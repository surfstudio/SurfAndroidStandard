package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.complex

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.navigation.Route.EXTRA_FIRST
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.sample.domain.SampleData
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultFragmentScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class ComplexDialogScreenConfigurator(args: Bundle) : DefaultFragmentScreenConfigurator(args) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, ComplexDialogScreenModule::class])
    internal interface ComplexDialogScreenComponent
        : ScreenComponent<ComplexDialogFragment>

    @Module
    internal class ComplexDialogScreenModule(route: ComplexDialogRoute)
        : DefaultCustomScreenModule<ComplexDialogRoute>(route)

    override fun createScreenComponent(parentComponentDefault: DefaultActivityComponent,
                                       defaultFragmentScreenModule: DefaultFragmentScreenModule,
                                       args: Bundle): ScreenComponent<*> {
        return DaggerComplexDialogScreenConfigurator_ComplexDialogScreenComponent.builder()
                .defaultActivityComponent(parentComponentDefault)
                .defaultFragmentScreenModule(defaultFragmentScreenModule)
                .complexDialogScreenModule(
                        ComplexDialogScreenModule(
                                ComplexDialogRoute(
                                        args.getSerializable(EXTRA_FIRST) as SampleData)))
                .build()
    }
}