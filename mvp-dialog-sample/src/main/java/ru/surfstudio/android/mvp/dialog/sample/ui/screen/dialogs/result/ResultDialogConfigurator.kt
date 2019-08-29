package ru.surfstudio.android.mvp.dialog.sample.ui.screen.dialogs.result

import android.os.Bundle
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultFragmentScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule

class ResultDialogConfigurator(args: Bundle) : DefaultFragmentScreenConfigurator(args) {

    @PerScreen
    @Component(
            dependencies = [DefaultActivityComponent::class],
            modules = [DefaultFragmentScreenModule::class, DialogModule::class]
    )
    internal interface DialogComponent : ScreenComponent<ResultDialogView>

    @Module
    internal class DialogModule(route: ResultDialogRoute) : DefaultCustomScreenModule<ResultDialogRoute>(route)

    override fun createScreenComponent(
            parentComponent: DefaultActivityComponent,
            defaultFragmentScreenModule: DefaultFragmentScreenModule,
            args: Bundle
    ): ScreenComponent<*> {
        return DaggerResultDialogConfigurator_DialogComponent.builder()
                .defaultActivityComponent(parentComponent)
                .defaultFragmentScreenModule(defaultFragmentScreenModule)
                .dialogModule(DialogModule(ResultDialogRoute()))
                .build()
    }
}